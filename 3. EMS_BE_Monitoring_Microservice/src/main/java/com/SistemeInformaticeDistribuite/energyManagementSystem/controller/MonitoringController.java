package com.SistemeInformaticeDistribuite.energyManagementSystem.controller;

import com.SistemeInformaticeDistribuite.energyManagementSystem.service.IMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    @Autowired
    IMonitoringService iMonitoringService;

    @GetMapping("/getmonitoring")
    public ResponseEntity<?> getMonitoringValues(HttpServletRequest accessToken, @RequestParam("device_id") Integer deviceId) throws IllegalAccessException {
        return iMonitoringService.getMonitoredValues(accessToken, deviceId);
    }

    @GetMapping("/getdif")
    public ResponseEntity<?> getMonitoringDifValues(@RequestParam("device_id") Integer deviceId) {
        return iMonitoringService.getDifValues(deviceId);
    }

    @GetMapping("/getsum")
    public ResponseEntity<?> getMonitoringSumValues(@RequestParam("device_id") Integer deviceId) {
        return iMonitoringService.getSumValues(deviceId);
    }

}
