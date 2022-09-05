package com.joffy.mqttdemo.mqtt.mesh;
import com.joffy.mqttdemo.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Automate {
    @Value("${msg_data}")
    public String msg_data;
    @Autowired
    MqttGateway mqttGateway;

    @Scheduled(fixedDelay = 10000)
    public void meshMock() {
        try {
            mqttGateway.senToMqtt(msg_data, "mqtt-test");
//            mqttGateway.senToMqtt(msg_data, "elevator-02");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("failed to create mesh type 1");
        }
    }

}
