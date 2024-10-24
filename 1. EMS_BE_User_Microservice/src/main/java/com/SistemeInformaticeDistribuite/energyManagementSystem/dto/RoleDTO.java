package com.SistemeInformaticeDistribuite.energyManagementSystem.dto;

import com.SistemeInformaticeDistribuite.energyManagementSystem.model.ERole;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private Integer id;
    private ERole role;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.role = role.getRole();
    }
}
