package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.DeviceDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.DeviceRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Device;
import com.SistemeInformaticeDistribuite.energyManagementSystem.repository.IDeviceRepository;
import com.SistemeInformaticeDistribuite.energyManagementSystem.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeviceServiceImpl implements IDeviceService {
    @Autowired
    IDeviceRepository iDeviceRepository;

    @Autowired
    JwtUtils jwtUtils;

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    @Override
    public ResponseEntity<?> addDevice(HttpServletRequest request, DeviceRequest addDeviceRequest) {
        String token = extractTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String userRole = jwtUtils.getUserRoleFromJwtToken(token);
        if (!userRole.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        Device device = new Device();
        device.setUserEmail(addDeviceRequest.getUserEmail());
        device.setAddress(addDeviceRequest.getAddress());
        device.setDescription(addDeviceRequest.getDescription());
        device.setMaximumConsumption(addDeviceRequest.getMaximumConsumption());

        device = iDeviceRepository.save(device);
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setUserEmail(device.getUserEmail());
        deviceDTO.setAddress(device.getAddress());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setMaximumConsumption(device.getMaximumConsumption());

        return ResponseEntity.ok(deviceDTO);
    }

    @Override
    public ResponseEntity<?> showAllDevices(String userEmail) {
        List<Device> devices = iDeviceRepository.findByUserEmail(userEmail);

        if (devices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(device -> {
                    DeviceDTO deviceDTO = new DeviceDTO();
                    deviceDTO.setId(device.getId());
                    deviceDTO.setUserEmail(device.getUserEmail());
                    deviceDTO.setAddress(device.getAddress());
                    deviceDTO.setDescription(device.getDescription());
                    deviceDTO.setMaximumConsumption(device.getMaximumConsumption());
                    return deviceDTO;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(deviceDTOs);
    }

    @Override
    public ResponseEntity<?> modifyDevice(HttpServletRequest request, DeviceRequest modifyDeviceRequest) {
        String token = extractTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String userRole = jwtUtils.getUserRoleFromJwtToken(token);
        if (!userRole.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        Device device = iDeviceRepository.findById(modifyDeviceRequest.getId()).orElse(null);

        if (device == null) {
            return ResponseEntity.notFound().build();
        }

        device.setAddress(modifyDeviceRequest.getAddress());
        device.setDescription(modifyDeviceRequest.getDescription());
        device.setMaximumConsumption(modifyDeviceRequest.getMaximumConsumption());

        device = iDeviceRepository.save(device);

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(device.getId());
        deviceDTO.setUserEmail(device.getUserEmail());
        deviceDTO.setAddress(device.getAddress());
        deviceDTO.setDescription(device.getDescription());
        deviceDTO.setMaximumConsumption(device.getMaximumConsumption());

        return ResponseEntity.ok(deviceDTO);
    }

    @Override
    public ResponseEntity<?> deleteDevice(HttpServletRequest request, Integer deviceId) {
        String token = extractTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String userRole = jwtUtils.getUserRoleFromJwtToken(token);
        if (!userRole.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        Optional<Device> deviceOptional = iDeviceRepository.findById(deviceId);

        if (deviceOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Device device = deviceOptional.get();

        iDeviceRepository.delete(device);

        return ResponseEntity.ok(Collections.singletonMap("message", "Device with id " + device.getId() + " deleted successfully"));
    }

    @Override
    public ResponseEntity<?> deleteDeviceByMail(HttpServletRequest request, String userEmail) {
        String token = extractTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String userRole = jwtUtils.getUserRoleFromJwtToken(token);
        if (!userRole.contains("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        List<Device> devices = iDeviceRepository.findByUserEmail(userEmail);
        if (devices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        for (Device d : devices) {
            iDeviceRepository.delete(d);
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "Devices deleted successfully"));
    }
}
