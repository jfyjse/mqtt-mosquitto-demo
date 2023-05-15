package com.joffy.mqttdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class MqttDemoApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MqttDemoApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MqttDemoApplication.class, args);
		System.out.println("http://localhost:8001/swagger-ui/#/mqtt-controller/publishUsingPOST");
		System.out.println("beco creds loaded \nsample data\n{\n" +
				"    \"topic\":\"xlI03d_hr-cabin\",\n" +
				"    \"message\": [{\"timestamp\":\"2022-06-17T02:03:51Z\",\"type\":\"iBeacon\",\"mac\":\"10C005091CD5\",\"bleName\":\"\",\"ibeaconUuid\":\"64E2FEFF5AF34D2C97C4643B70CD82EB\",\"ibeaconMajor\":4,\"ibeaconMinor\":1,\"rssi\":-50,\"ibeaconTxPower\":-70,\"battery\":0}]\n" +
				"\n" +
				"}");
	}

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.joffy.mqttdemo")).build();
	}

}
