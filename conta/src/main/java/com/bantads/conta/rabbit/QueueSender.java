package com.bantads.conta.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class QueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(Message message) {
        rabbitTemplate.convertAndSend("saga", this.toJson(message));
    }
    
    public void sendAtualizacao(Integer codClie, String operac) {//aConta, aMovi
    	Message message = new Message();
    	message.setOperacao(operac);
    	message.setData(codClie.toString());
    	rabbitTemplate.convertAndSend("conta", toJson(message));
    }

	private String toJson(Object message) {
		Gson gson = new Gson();
		return gson.toJson(message);
	}
}
