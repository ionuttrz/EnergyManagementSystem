package com.SistemeInformaticeDistribuite.energyManagementSystem.dto.response;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.DeviceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private DeviceDTO user;
    private String accessToken;
}
