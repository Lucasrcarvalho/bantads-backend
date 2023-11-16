package com.bantads.gerente;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bantads.gerente.entity.Gerente;
import com.bantads.gerente.entity.GerenteDTO;
import com.bantads.gerente.entity.GerenteRepository;
import com.bantads.gerente.rabbit.Message;
import com.bantads.gerente.rabbit.QueueSender;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

@RestController
@RequestMapping("/gerente")
public class Controller {
	
	@Autowired
	public GerenteRepository repo;
	
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
			ObjectNode node =  mapper.readValue(message.getData(), ObjectNode.class);
			
			if (message.getOperacao().equals("new")) {
				JsonNode clieCod= node.get("cliente").get("codigo");
				if (!message.getEstorno()) {
					List<GerenteDTO> list = this.getGerentes();
					if (list!=null && list.size()>0) {
						List<Integer> codigos = new ArrayList<>();
						for (GerenteDTO gerent: list) {
							codigos.add(gerent.getCodigo());
						}
						node.putArray("gerentes");
						ArrayNode array = (ArrayNode) node.get("gerentes");
						for (Integer numb: codigos) {
							array.add(numb);
						}
						message.setData(node.toString());
					} else {
						message.setErro("Nenhum gerente encontrado");
						message.setCodErro(409);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
        this.queueSender.send(message);
    }
	
	private List<GerenteDTO> getGerentes() {
		return this.repo.findAll();
	}
	
	@GetMapping("")
	ResponseEntity<Object> listarTodosGerente() {
		List<GerenteDTO> lista = repo.findAll();
		List<GerenteDTO> gerentes = lista.stream().map(e -> mapper.map(e, GerenteDTO.class)).collect(Collectors.toList());
		return new ResponseEntity<>(new JsonResponse(true, "Procurando...", gerentes), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarGerente(@PathVariable("codigo") Integer codigo) {
		GerenteDTO aux_gere = repo.findById(codigo).get();
		if (aux_gere == null) {
			return new ResponseEntity<>(new JsonResponse(false, "Gerente não encontrado", null), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new JsonResponse(true, "Gerente encontrado", mapper.map(aux_gere, GerenteDTO.class)), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> removerGerente(@PathVariable("codigo") Integer codigo) {
		GerenteDTO aux_gere = repo.findById(codigo).get();
		if (aux_gere != null) {
			repo.deleteById(aux_gere.getCodigo());
			return new ResponseEntity<>(new JsonResponse(true, "Gerente removido com sucesso!", null), HttpStatus.OK);
		}
		return new ResponseEntity<>(new JsonResponse(false, "Gerente não encontrado!", null), HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> alterarGerente(@PathVariable("id") Integer codigo, @RequestBody GerenteDTO gerente) {
		GerenteDTO aux_gere = repo.findById(codigo).get();
		if (aux_gere != null) {
			aux_gere.setCpf(gerente.getCpf());
			aux_gere.setNome(gerente.getNome());
			aux_gere.setEmail(gerente.getEmail());
			repo.save(aux_gere);
			return new ResponseEntity<>(new JsonResponse(true, "Alterado com sucesso!", aux_gere), HttpStatus.OK);
		}
		return new ResponseEntity<>(new JsonResponse(false, "Registro não encontrado!", null), HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/")
	public ResponseEntity<Object> inserir(@RequestBody Gerente gerente) {
		repo.save(mapper.map(gerente, GerenteDTO.class));
		GerenteDTO aux_gere = this.repo.findByCpf(gerente.getCpf());
		return new ResponseEntity<>(new JsonResponse(true, "Inserido com sucesso!", mapper.map(aux_gere, GerenteDTO.class)), HttpStatus.OK);
	}
}
