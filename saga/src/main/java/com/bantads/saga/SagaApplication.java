package com.bantads.saga;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@SpringBootApplication
//@Configuration( proxyBeanMethods  =  false )  
//@EnableAutoConfiguration(exclude ={DataSourceAutoConfiguration.class}) 
public class SagaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SagaApplication.class, args);
	}

	
}
