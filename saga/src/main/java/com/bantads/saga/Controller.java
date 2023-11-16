package com.bantads.saga;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class Controller {

	public static Map<Long, Message> completos = new HashMap<>();

	@Autowired
	private QueueSender queueSender;
	
	@PostMapping("/autocadastro")
	ResponseEntity autoCadastroRequest(@RequestBody String json) {
		Long transacao = new Date().getTime();
		Message message = new Message();
		message.setCodigo(transacao);
		message.setOrigem("saga");
		message.setOperacao("new");
		List<String> dests = new ArrayList<>();
		message.getDests().add("auth");
			message.getDests().add("cliente");
			message.getDests().add("gerente");
			message.getDests().add("conta");
		message.setData(json);
		message.setEtapa(0);
		Controller.completos.remove(transacao);
		this.queueSender.send(message);
		Message retorno = waitRPC(transacao);
		if (retorno.getCodErro()>300) {
			System.out.println("Estornando operação");
			if (retorno.getEtapa()>=0) {
				retorno.setEstorno(true);
				this.queueSender.send(retorno);
			}
			return ResponseEntity.status(retorno.getCodErro()).contentType(MediaType.TEXT_PLAIN).body(retorno.getErro());
		} else {
			return ResponseEntity.ok(retorno.getData());
		}
	}
	
	private Message waitRPC(Long transacao) {
		Integer i = 0;
		Integer time = 100;
		while (i < 5) {
			if (Controller.completos.containsKey(transacao)) {
				Message message = Controller.completos.get(transacao);
				Controller.completos.remove(transacao);
				return message;
			}
			i++;
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return new Message(500, "Erro no Thread.sleep");
			}
			time = time * 2;//100 + 200 + 400 + 800 + 1600 = 3100
		}
		return new Message(408, "Não foi possivel concluir as operações");
	}
}
