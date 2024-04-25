package com.joffy.mqttdemo.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.JavaVersion;
import org.springframework.boot.system.SystemProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringVersion;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Objects;
import java.util.stream.StreamSupport;

@Configuration
public class MqttConfig {
    @Value("${server_url}")
    private String server_url;

    @Value("${user_name}")
    private String user_name;

    @Value("${password}")
    private String password;

    @Value("${sub_topic}")
    private String sub_topic;

    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{server_url});
        options.setUserName(user_name);
        String pwd = password;
        options.setPassword(pwd.toCharArray());
        options.setCleanSession(true);
        options.setConnectionTimeout(30);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);
        factory.setConnectionOptions(options);
        System.out.println("connected 56 auth");
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        //clientId is generated using a random number
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory());

        //MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory());
        System.out.println("outbound client id " + messageHandler.getClientId());
        System.out.println("spring " + SpringVersion.getVersion());
        System.out.println("java " + JavaVersion.getJavaVersion().toString());
        System.out.println("jdk  " + SystemProperties.get("java.version"));
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        System.out.println("connected 80 outbound");
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        String client = MqttAsyncClient.generateClientId();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(client,
                mqttPahoClientFactory(), sub_topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        System.out.println("connected 97 inbound");
        System.out.println("inbound client = "+client);
        return adapter;

    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
//                System.out.println(message.getHeaders().get("mqtt_receivedTopic"));
//                System.out.println(topic);
                System.out.println("\n============New_Data=============\n" +
                        "data from gateway/topic ---- " + topic);
               // System.out.println("getclass \n"+message.getClass().toString() +"getpayload \n"+message.getPayload() +
                       // "getheader"+message.getHeaders());


                String stringArray = message.getPayload().toString();
                //System.out.println(stringArray);
                // Create ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse JSON
                JsonNode rootNode = null;
                try {
                    rootNode = objectMapper.readTree(stringArray);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                // Extract "major" and "minor" values using Java Streams
                StreamSupport.stream(rootNode.path("data").path("value").path("device_list").spliterator(), false)
                        .forEach(device -> {
                            String major = device.path("major").asText();
                            String minor = device.path("minor").asText();
                            System.out.println("Major: " + major + ", Minor: " + minor);
                        });

                try {
                    new JSONObject(stringArray);
                } catch (JSONException e) {
                    try {
                        JSONArray array = new JSONArray(stringArray);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String uuid = object.getString("ibeaconUuid");
                            int major = object.getInt("ibeaconMajor");
                            int minor = object.getInt("ibeaconMinor");
                            int rssi = object.getInt("rssi");
                            int slno = i + 1;
                           // System.out.println("SLNO : " + slno + "\nuuid : " + uuid + " \nmajor : " + major + "\nminor : " + minor + "\nrssi : " + rssi + "\n" +
                            //        "---------------------------------------");
                        }

                    } catch (JSONException ne) {
                        System.out.println("Message in data is not in JsonArray Format\nmessage: " + message.getPayload());
                    }
                }
                System.out.println("======++====Data_End====++=======");


//                if (topic.equals(sub_topic)) {
//                    System.out.println("subbed topic " + message.getPayload());
//                    JSONArray array = new JSONArray(message.getPayload().toString());
//                    for (int i = 0; i < array.length(); i++) {
//                        String test_uuid = "B7F4631BF8BC4BBDB407612A18E254D1";
//                        int test_major =1;
//                        int test_minor =3;
//                        JSONObject object = array.getJSONObject(i);
//                        String uuid =object.getString("ibeaconUuid");
//                        int major = object.getInt("ibeaconMajor");
//                        int minor = object.getInt("ibeaconMinor");
//                        int rssi =object.getInt("rssi");
//                        System.out.println("---found--- uuid= "+uuid + " major= "+major + " minor= "+minor+" rssi= "+rssi);
////                        if (Objects.equals(uuid, test_uuid) && major==test_major && minor == test_minor){
////                            System.out.println("---found--- uuid= "+uuid + " major= "+major + " minor= "+minor+" rssi= "+rssi);
////                        }
//                    }
//                } else {
//                    System.out.println("othr topic / # topic \n " + message.getPayload());
//                }

            }
        };
    }
}
