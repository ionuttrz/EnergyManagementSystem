package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.LoginRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.RegisterRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.UserRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.response.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface IUserService {
    ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest);

    ResponseEntity<?> register(RegisterRequest registerRequest);
    ResponseEntity<?> registerAsAdmin(RegisterRequest registerRequest);
    ResponseEntity<?> showAllUsers(Authentication authentication);
    ResponseEntity<?> modifyUser(Authentication authentication, UserRequest user);
    ResponseEntity<?> deleteUser(Authentication authentication, String username);
}
