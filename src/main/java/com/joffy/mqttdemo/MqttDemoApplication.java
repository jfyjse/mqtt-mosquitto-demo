package com.joffy.mqttdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MqttDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttDemoApplication.class, args);
	}

}
