package com.bantads.saga;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


@Component
public class QueueConsumer {

	@Autowired
	private QueueSender queueSender;
	
	@RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
		Gson gson = new Gson();
		JsonElement json = JsonParser.parseString(fileBody);
		Message message = gson.fromJson(json, Message.class);
		System.out.println(message.getOperacao() + " - " + message.getCodErro());
		if (message.getErro().isEmpty() && message.getDests().size()-1<=message.getEtapa()) {
			System.out.println(message.getOperacao() + " - " + message.getCodErro() + " - Completo");
			Controller.completos.put(message.getCodigo(), message);
		} else {
			Boolean hadErro = message.getCodErro()>=300;
			if (!message.getEstorno() && !hadErro) {
				message.setEtapa(message.getEtapa()+1);
			} else {
				message.setEtapa(message.getEtapa()-1);
			}
			if (message.getCodErro()>=300) {
				Controller.completos.put(message.getCodigo(), message);
				System.out.println(message.getOperacao() + " - " + message.getCodErro() + " - Completo com falha");
			} else {
				message.setDestino(message.getDests().get(message.getEtapa()));
				System.out.println(message.getOperacao() + " - " + message.getCodErro() + " - Send");
				this.queueSender.send(message);
			}
		}
    }	
}
