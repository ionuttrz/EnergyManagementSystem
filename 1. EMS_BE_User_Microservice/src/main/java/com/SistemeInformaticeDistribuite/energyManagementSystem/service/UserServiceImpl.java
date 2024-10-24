package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.UserDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.LoginRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.RegisterRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.request.UserRequest;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.response.AuthenticationResponse;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.response.UserResponse;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.ERole;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Role;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.User;
import com.SistemeInformaticeDistribuite.energyManagementSystem.repository.IRoleRepository;
import com.SistemeInformaticeDistribuite.energyManagementSystem.repository.IUserRepository;
import com.SistemeInformaticeDistribuite.energyManagementSystem.security.JwtUtils;
import com.SistemeInformaticeDistribuite.energyManagementSystem.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IRoleRepository iRoleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @Override
    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        User user = iUserRepository.findByEmail(userDetails.getEmail()).orElse(null);
        UserDTO userDTO = new UserDTO(user);
        String role = user.getRoles().toString();
        System.out.println(role);
        Pattern pattern = Pattern.compile("role=(\\w+)");
        Matcher matcher = pattern.matcher(role);
        String extractedRole = null;
        if (matcher.find()) {
            extractedRole = matcher.group(1);
            System.out.println(extractedRole); 
        } else {
            System.out.println("No match found");
        }
        String jwt = jwtUtils.generateJwtToken(authentication, extractedRole);
        return ResponseEntity.ok(new AuthenticationResponse(userDTO, jwt));

    }

    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (iUserRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        // Create new user's account
        User user = new User();
        Role userRole = iRoleRepository.findByRole(ERole.CLIENT).orElse(null);

        if (userRole == null) {
            Role tempRole = new Role();
            tempRole.setRole(ERole.CLIENT);
            userRole = iRoleRepository.save(tempRole);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        user = iUserRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), registerRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication, ERole.CLIENT.toString());

        return ResponseEntity.ok(new AuthenticationResponse(new UserDTO(user), jwt));
    }

    @Override
    public ResponseEntity<?> registerAsAdmin(RegisterRequest registerRequest) {
        if (iUserRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        // Create new user's account
        User user = new User();
        Role userRole = iRoleRepository.findByRole(ERole.ADMIN).orElse(null);

        if (userRole == null) {
            Role tempRole = new Role();
            tempRole.setRole(ERole.ADMIN);
            userRole = iRoleRepository.save(tempRole);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));

        user = iUserRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), registerRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication, ERole.ADMIN.toString());

        return ResponseEntity.ok(new AuthenticationResponse(new UserDTO(user), jwt));
    }

    //@Secured("ADMIN")
    @Override
    public ResponseEntity<?> showAllUsers(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Handle the case where the user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }
        List<User> list = iUserRepository.findAllClients();
        System.out.println("Lista cu useri: " + list);
        List<UserResponse> userResponses = new ArrayList<UserResponse>();
        for (User m : list) {
            UserResponse userResponse = new UserResponse();
            userResponse.setName(m.getName());
            userResponse.setEmail(m.getEmail());
            userResponse.setRoles(m.getRoles().toString());

            userResponses.add(userResponse);
        }
        return ResponseEntity.ok(userResponses);
    }

    private boolean userHasAdminRole(Authentication authentication) {
        String email = authentication.getName();
        User user = iUserRepository.findByEmail(email).orElse(null);
        if(user != null) {
            Set<Role> roles = user.getRoles();
            boolean hasAdminRole = roles.stream()
                    .anyMatch(role -> role.getRole() == ERole.ADMIN);
            return hasAdminRole;
        }
        return false;
    }

    @Override
    public ResponseEntity<?> modifyUser(Authentication authentication, UserRequest user) {
        if (!userHasAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        String userEmailToChange = user.getEmail();
        User userToUpdate = iUserRepository.findByEmail(userEmailToChange).orElse(null);

        userToUpdate.setName(user.getName());
        iUserRepository.save(userToUpdate);

        System.out.println("User modified!");
        return ResponseEntity.ok(userToUpdate);
    }

    public ResponseEntity<?> deleteUser(Authentication authentication, String userEmail) {
        if (!userHasAdminRole(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        iUserRepository.deleteByEmail(userEmail);
        return ResponseEntity.ok(Collections.singletonMap("message", "User " + userEmail + " deleted successfully"));
    }
}
