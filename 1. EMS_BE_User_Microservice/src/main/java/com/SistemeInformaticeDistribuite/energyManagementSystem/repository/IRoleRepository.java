package com.SistemeInformaticeDistribuite.energyManagementSystem.repository;

import com.SistemeInformaticeDistribuite.energyManagementSystem.model.ERole;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(ERole role);
}
