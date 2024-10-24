package com.SistemeInformaticeDistribuite.energyManagementSystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "monitoring")
public class Monitoring {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "timestamp")
    private Date timestamp;

    @Column(name = "measurement_value")
    private Float measurementValue;
}
