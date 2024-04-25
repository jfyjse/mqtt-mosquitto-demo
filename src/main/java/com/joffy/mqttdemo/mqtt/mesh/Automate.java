//package com.joffy.mqttdemo.mqtt.mesh;
//
//import com.joffy.mqttdemo.mqtt.MqttGateway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class Automate {
//    @Value("${msg_data}")
//    public String msg_data;
//    @Autowired
//    MqttGateway mqttGateway;
//
//    @Scheduled(fixedDelay = 5000)
//    public void meshMock() {
//        try {
//            int i=0;
//            //mqttGateway.senToMqtt("[{\"timestamp\":\"2022-06-17T02:03:51Z\",\"type\":\"iBeacon\",\"mac\":\"10C005091CD5\",\"bleName\":\"\",\"ibeaconUuid\":\"20354D7AE4FE47AF8FF6187BCA92F3F9\",\"ibeaconMajor\":1,\"ibeaconMinor\":1,\"rssi\":-50,\"ibeaconTxPower\":-70,\"battery\":0}]\n", "test");
//            mqttGateway.senToMqtt(msg_data, "1");
//            //10000 ble unique
//            //500 mesh unique
//            //1 mesh 20 ble
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.out.println("failed to create mesh type 1");
//        }
//    }
//
//}
