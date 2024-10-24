package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.DeviceDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.DeviceRequest;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface IDeviceService {
    ResponseEntity<?> addDevice(HttpServletRequest accessToken, DeviceRequest deviceRequest);
    ResponseEntity<?> showAllDevices(String userEmail);
    ResponseEntity<?> modifyDevice(HttpServletRequest accessToken, DeviceRequest deviceRequest);
    ResponseEntity<?> deleteDevice(HttpServletRequest accessToken,Integer deviceId);
    ResponseEntity<?> deleteDeviceByMail(HttpServletRequest accessToken, String userEmail);

}
