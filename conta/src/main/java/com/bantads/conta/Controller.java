package com.bantads.conta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bantads.conta.entity.ContaDTO;
import com.bantads.conta.entity.ContaRDTO;
import com.bantads.conta.entity.ContaRRepository;
import com.bantads.conta.entity.ContaRepository;
import com.bantads.conta.entity.MovimentoDTO;
import com.bantads.conta.entity.MovimentoRDTO;
import com.bantads.conta.entity.MovimentoRRepository;
import com.bantads.conta.entity.MovimentoRepository;
import com.bantads.conta.rabbit.Message;
import com.bantads.conta.rabbit.QueueSender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

@RestController
@EnableJpaRepositories
@RequestMapping("/conta")
public class Controller {
	
	@Autowired
	public MovimentoRepository repoMov;
	
	@Autowired
	public MovimentoRRepository repoMovR;
	
	@Autowired
	public ContaRepository repoConta;
	
	@Autowired
	public ContaRRepository repoContaR;
	
	@Autowired
	private ModelMapper mapper; 
	
	@Autowired
	private QueueSender queueSender;
	
	@RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
		ObjectMapper mapper = new ObjectMapper();
	    Gson gson = new Gson();
		Message message = gson.fromJson(fileBody, Message.class);
		try {
			String operacao = message.getOperacao();
			if (operacao.equals("aConta")) {
				this.atualizaConta(message);
			} else if (operacao.equals("aMovi")) {
				this.atualizaMovi(message);
			} else { 
				if (operacao.equals("new")) {
					ObjectNode node =  mapper.readValue(message.getData(), ObjectNode.class);
					JsonNode clieCod= node.get("cliente");
					clieCod = clieCod.get("codigo");
					ArrayNode array = (ArrayNode) node.get("gerentes");
					if (!message.getEstorno()) {
						Double salario = this.getSalario(node);
						ContaDTO result = this.novaConta(clieCod.asInt(), array, salario);
						if (result==null) {
							message.setErro("Conta já cadastrada");
							message.setCodErro(409);
						}
					}
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setErro(e.getMessage());
			message.setCodErro(500);
		}
        this.queueSender.send(message);
    }	
	
	private void atualizaMovi(Message message) {
		MovimentoDTO movi = this.repoMov.findByClieCodigo(Integer.getInteger(message.getData())); 
		MovimentoRDTO moviR = this.mapper.map(movi, MovimentoRDTO.class);
		this.repoMovR.save(moviR);
		this.repoMov.delete(movi);
	}

	private void atualizaConta(Message message) {
		ContaDTO conta = this.repoConta.findByClieCodigo(Integer.parseInt(message.getData())); 
		ContaRDTO contaR = this.mapper.map(conta, ContaRDTO.class);
		this.repoContaR.save(contaR);
		this.repoConta.delete(conta);
	}

	@GetMapping("/deposito/{valor}/{codigo}")
	public ResponseEntity doDeposito(@PathVariable Double valor, @PathVariable(name = "codigo") Integer clieCod) {
		MovimentoDTO result = null;
		try {
			ContaDTO conta = this.mapper.map(this.getConta(clieCod), ContaDTO.class);
			if (conta==null) {
				return ResponseEntity.status(400).contentType(MediaType.TEXT_PLAIN).body("Conta não encontrada");
			}
			MovimentoDTO mov = new MovimentoDTO("C");
			mov.setContOrigem(clieCod);
			mov.setValor(valor);
			this.repoMov.save(mov);
			this.queueSender.sendAtualizacao(clieCod, "aMovi");
			
			conta.setSaldo(conta.getSaldo()+valor);
			this.repoConta.delete(conta);
			this.repoConta.save(conta);
			this.queueSender.sendAtualizacao(clieCod, "aConta");
			
			result = mov;
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("saque/:valor")
	public ResponseEntity doSaque(@RequestParam Double valor, @RequestParam Integer usuaCod) {
		MovimentoDTO result = null;
		try {
			ContaDTO conta = this.mapper.map(this.getConta(usuaCod), ContaDTO.class);
			if (conta==null) {
				return ResponseEntity.status(400).contentType(MediaType.TEXT_PLAIN).body("Conta não encontrada");
			}
			if (conta.getSaldo()+conta.getLimite()>=valor) {
				MovimentoDTO mov = new MovimentoDTO("D");
				mov.setContDestino(usuaCod);
				mov.setValor(valor);
				this.repoMov.save(mov);
				this.queueSender.sendAtualizacao(usuaCod, "aMovi");
				
				conta.setSaldo(conta.getSaldo()-valor);
				this.repoConta.delete(conta);
				this.repoConta.save(conta);
				this.queueSender.sendAtualizacao(usuaCod, "aConta");
				
				result = mov;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("transferencia/:destino/:valor")
	public ResponseEntity doSaque(@RequestParam Integer destino, @RequestParam Double valor, @RequestParam Integer usuaCod) {
		MovimentoDTO result = null;
		try {
			ContaDTO contaOrig = this.mapper.map(this.getConta(usuaCod), ContaDTO.class);
			ContaDTO contaDest = this.mapper.map(this.getConta(destino), ContaDTO.class);
			if (contaOrig==null) {
				return ResponseEntity.status(400).contentType(MediaType.TEXT_PLAIN).body("Conta de origem não encontrado");
			}
			if (contaDest==null) {
				return ResponseEntity.status(400).contentType(MediaType.TEXT_PLAIN).body("Conta de destino não encontrado");
			}
			if (contaOrig.getSaldo()+contaOrig.getLimite()>=valor) {
				MovimentoDTO movOrig = new MovimentoDTO("D");
				movOrig.setContOrigem(usuaCod);
				movOrig.setContDestino(destino);
				movOrig.setValor(valor);
				contaOrig.setSaldo(contaOrig.getSaldo()-valor);
				this.repoMov.delete(movOrig);
				this.repoMov.save(movOrig);
				this.repoConta.save(contaOrig);
				this.queueSender.sendAtualizacao(usuaCod, "aMovi");
				this.queueSender.sendAtualizacao(usuaCod, "aConta");

				MovimentoDTO movDest = new MovimentoDTO("C");
				movDest.setContOrigem(usuaCod);
				movDest.setValor(valor);
				contaDest.setSaldo(contaDest.getSaldo()+valor);
				this.repoMov.delete(movDest);
				this.repoMov.save(movDest);
				this.repoConta.save(contaDest);
				this.queueSender.sendAtualizacao(destino, "aMovi");
				this.queueSender.sendAtualizacao(destino, "aConta");
				
				result = movOrig;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<ContaRDTO> getContaRest(@PathVariable Integer codigo) {
		ContaRDTO result = null;
		try {
			result = this.repoContaR.findByClieCodigo(codigo);
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return ResponseEntity.ok().body(result);
	}
	
	private Double getSalario(ObjectNode node) {
		JsonNode salNode =  node.get("cliente");
		if (salNode != null) {
			salNode = ((ObjectNode) salNode).get("salario");
			if (salNode != null) {
				return salNode.asDouble();
			}
		}
		return null;
	}

	private ContaRDTO getConta(Integer codClie) {
		return this.repoContaR.findByClieCodigo(codClie);
	}
	
	public ContaDTO novaConta(Integer codClie, ArrayNode gerentes, Double salario) {
		ContaDTO contResult = null;
		try {	
			ContaRDTO contaDTO = this.getConta(codClie);
			if (contaDTO==null) {
				ContaDTO conta = new ContaDTO(codClie);
				this.setNumeroConta(conta);
				this.setGerente(conta, gerentes);
				conta.setStatus("P");
				if (salario!=null) {
					conta.setLimite(salario/2);
				}
				conta.setSaldo(0.0);
				this.repoConta.save(conta);
				this.queueSender.sendAtualizacao(codClie, "aConta");
				contResult = conta;
			}
		} catch (Exception e) {
			e.printStackTrace();
			contResult = null;
		}
		return contResult;
	} 
	
	private void setNumeroConta(ContaDTO conta) {
		List<ContaRDTO> list = this.repoContaR.findAll();
		Integer numero = 1000;
		if (list!=null && list.size()>0) {
			numero = list.get(list.size()-1).getNumero()+1;
		} 			
		conta.setNumero(numero);
	}
	
	private void setGerente(ContaDTO conta, ArrayNode codigos) {
		List<ContaRDTO> list = this.repoContaR.findAll();
		if (list!=null && list.size()>0) {
			Integer codMenosPres = this.getMenosPresente(list); 
			if (!this.contain(codigos, codMenosPres)) {
				conta.setGere_codigo(codMenosPres);
			} else if (list.size()<codigos.size()) {
				Integer codigo = 0;
				for (int i=0; i<codigos.size()-1; i++) {
					if (!this.contain(list, codigos.get(i).asInt())) {
						codigo = codigos.get(i).asInt();
					}
				}
				conta.setGere_codigo(codigo);
			}
		} else {
			conta.setGere_codigo(codigos.get(0).asInt());
		}
	}
	
	private boolean contain(ArrayNode codigos, Integer codMenosPres) {
		for (int i = 0; i<codigos.size(); i++) {
			if (codigos.get(i).asInt()==codMenosPres) {
				return true;
			}
		}
		return false;
	}

	private Integer getMenosPresente(List<ContaRDTO> list) {
		Map<Integer, Integer> map = new HashMap<>();
		List<Integer> keys = new ArrayList<>();
		for (ContaRDTO contaDTO: list) {
			Integer key = contaDTO.getGere_codigo();
			if (map.containsKey(key) ) {
				map.put(key, map.get(key)+1);
			} else {
				map.put(key, 1);
				keys.add(key);
			}
		}
		Integer menor = Integer.MAX_VALUE;
		for (Integer key: keys) {
			if (map.get(key) < menor) {
				menor = map.get(key);
			}
		}
		return menor;
	}

	private Boolean contain(List<ContaRDTO> list, Integer value) {
		for (ContaRDTO contaDTO: list) {
			if (contaDTO.getGere_codigo()==value) {
				return true;
			}
		}
		return false;
	}

}
