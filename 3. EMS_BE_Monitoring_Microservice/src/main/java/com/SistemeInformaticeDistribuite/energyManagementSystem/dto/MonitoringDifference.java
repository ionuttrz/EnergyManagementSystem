package com.SistemeInformaticeDistribuite.energyManagementSystem.dto;

import lombok.Data;

import java.util.Date;


@Data
public class MonitoringDifference {
    private Date timestamp;
    private double difference;

    public MonitoringDifference(Date timestamp, double difference) {
        this.timestamp = timestamp;
        this.difference = difference;
    }
}
