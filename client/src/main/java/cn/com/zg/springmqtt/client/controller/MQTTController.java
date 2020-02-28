package cn.com.zg.springmqtt.client.controller;

import cn.com.zg.springmqtt.client.service.MQTTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mqtt")
public class MQTTController {

    @Autowired
    private MQTTService mqttService;

    @GetMapping("send")
    public String sendMessage(@RequestParam("message") String msg,
                              @RequestParam("topic") String topic,
                              @RequestParam("qos") int qos) {
        return mqttService.pubMsg(topic,msg, qos);
    }

    @GetMapping("sub")
    public String subMessage(@RequestParam("topic") String topic,
                              @RequestParam("qos") int qos) {
        return mqttService.subscribe(topic,qos);
    }

    @GetMapping("start")
    public String mqttStart() {
        return mqttService.start();
    }

    @GetMapping("close")
    public String close() {
        return mqttService.disConnectMqtt();
    }

    @GetMapping("unsubscribe")
    public String unsubscribe(@RequestParam("topic") String topic) {
        return mqttService.unsubscribe(topic);
    }



}
