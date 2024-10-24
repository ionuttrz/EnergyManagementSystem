package com.SistemeInformaticeDistribuite.energyManagementSystem.controller;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.LoginRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.RegisterRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.UserRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.response.AuthenticationResponse;
import com.SistemeInformaticeDistribuite.energyManagementSystem.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    IUserService iUserService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return iUserService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return iUserService.register(registerRequest);
    }

    @PostMapping("/registerasadmin")
    public ResponseEntity<?> registerAsAdmin(@RequestBody RegisterRequest registerRequest) {
        return iUserService.registerAsAdmin(registerRequest);
    }

    @GetMapping("/showallusers")
    public ResponseEntity<?> showAllUsers(Authentication accessToken) {
        return iUserService.showAllUsers(accessToken);
    }

    @PutMapping("/modifyuser")
    public ResponseEntity<?> modifyUser(Authentication accessToken, @RequestBody UserRequest user) {
        return iUserService.modifyUser(accessToken, user);
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(Authentication accessToken, @RequestParam(name = "userEmail")String userEmail){
        return iUserService.deleteUser(accessToken, userEmail);
    }
}
