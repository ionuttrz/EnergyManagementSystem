package com.SistemeInformaticeDistribuite.energyManagementSystem.dto.response;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserDTO user;
    private String accessToken;
}
