package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDifference;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Monitoring;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface IMonitoringService {
    ResponseEntity<Monitoring> addMonitoring(MonitoringDTO monitoringDTO);

    ResponseEntity<?> getMonitoredValues(HttpServletRequest request, Integer deviceId) throws IllegalAccessException;

    ResponseEntity<List<MonitoringDifference>> getDifValues(Integer deviceId);

    ResponseEntity<Map<String, Double>> getSumValues(Integer deviceId);
    //ResponseEntity<Map<String, Double>> getAverageValues(Integer deviceId);
}
