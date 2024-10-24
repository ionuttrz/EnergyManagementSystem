package com.SistemeInformaticeDistribuite.energyManagementSystem.controller;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.publisher.RabbitMQJsonProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitoring")
public class MessageJsonController {

    private RabbitMQJsonProducer jsonProducer;

    public MessageJsonController(RabbitMQJsonProducer jsonProducer) {
        this.jsonProducer = jsonProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody MonitoringDTO monitoringDTO){
        jsonProducer.sendJsonMessage(monitoringDTO);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }

}
