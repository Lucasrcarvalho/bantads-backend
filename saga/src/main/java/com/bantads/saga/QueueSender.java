package com.bantads.saga;

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
    	System.out.println("Enviando estorno?("+message.getEstorno()+") para: "+ message.getDests().get(message.getEtapa()) +" => etapa: " + message.getEtapa());
        rabbitTemplate.convertAndSend(message.getDests().get(message.getEtapa()), this.toJson(message));
    }
    
    public String toJson(Object message) {
		Gson gson = new Gson();
		return gson.toJson(message);
	}
}
