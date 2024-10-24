package com.SistemeInformaticeDistribuite.energyManagementSystem.consumer;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.service.IMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonConsumer {

    @Autowired
    IMonitoringService iMonitoringService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.json.name}")
    public void consumeJsonMessage(MonitoringDTO monitoringDTO){
        LOGGER.info(String.format("Received JSON message -> %s", monitoringDTO.toString()));
        iMonitoringService.addMonitoring(monitoringDTO);
    }
}
