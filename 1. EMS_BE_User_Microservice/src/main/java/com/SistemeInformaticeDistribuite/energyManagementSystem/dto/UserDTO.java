package com.SistemeInformaticeDistribuite.energyManagementSystem.dto;

import com.SistemeInformaticeDistribuite.energyManagementSystem.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private String roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.roles = user.getRoles().stream().map((r) -> r.getRole()).collect(Collectors.toSet()).toString();
    }
}
