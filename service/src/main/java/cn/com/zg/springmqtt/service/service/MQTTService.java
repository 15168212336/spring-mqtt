package cn.com.zg.springmqtt.service.service;

import cn.com.zg.springmqtt.service.callback.PushCallback;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;

@Service
public class MQTTService {

    public static final String HOST = "tcp://localhost:1883";
    public static final String TOPIC_RECEIVE = "test_topic1";
    public static final String TOPIC_SEND = "test_topic1";
    private static final String clientid = "server1";
    private MqttClient client;
    private String userName = "admin";
    private String passWord = "123456";

    private ScheduledExecutorService scheduler;

    public String start() {
        if (client != null && client.isConnected()) {
            return "mqtt is started!";
        }
        try {
            // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientid, new MemoryPersistence());

            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调
            client.setCallback(new PushCallback());
//            MqttTopic topic = client.getTopic(TOPIC_SEND);
            //setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//            options.setWill(topic, "service-close".getBytes(), 2, false);
            client.connect(options);

            //订阅消息
//            int[] Qos  = {2};
//            String[] topic1 = {TOPIC_SEND};
//            client.subscribe(topic1, Qos);
            return "ok";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String SendMessage(String msg , int qos) {
        if (msg == null) {
            return "msg is null!";
        }
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setRetained(true);
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        try {
            client.publish(TOPIC_SEND,mqttMessage);
            return "success";
        } catch (MqttException e) {
            e.printStackTrace();
            return "error";
        }

    }

    /**
     * 接收消息
     * @param topic
     * @return
     */
    public String subscribe(String topic,int qos) {
        //订阅消息
        int[] Qos  = {qos};
        String[] topic1 = {topic};
        try {
            client.subscribe(topic1, Qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    /**
     * 发送消息
     * @param topic
     * @param msg
     * @param qos
     * @return
     */
    public String pubMsg(String topic,String msg , int qos) {
        if (msg == null) {
            return "msg is null!";
        }
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setRetained(true);
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        try {
            client.publish(topic,mqttMessage);
            return "success";
        } catch (MqttException e) {
            e.printStackTrace();
            return "error";
        }

    }

    public String disConnectMqtt() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    public String unsubscribe(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return "ok";
    }

}