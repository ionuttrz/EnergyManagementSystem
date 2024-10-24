package com.SistemeInformaticeDistribuite.energyManagementSystem.dto;


import lombok.Data;

import java.util.Date;

@Data
public class MonitoringDTO {
    private Integer deviceId;
    private Date timestamp;
    private Float measurementValue;
}
