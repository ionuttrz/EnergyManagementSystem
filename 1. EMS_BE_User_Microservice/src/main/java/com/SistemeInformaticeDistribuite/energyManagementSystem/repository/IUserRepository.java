package com.SistemeInformaticeDistribuite.energyManagementSystem.repository;

import com.SistemeInformaticeDistribuite.energyManagementSystem.model.ERole;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Role;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.role = 'CLIENT'")
    List<User> findAllClients();

    @Transactional
    @Modifying
    void deleteByEmail(@Param("userEmail") String userEmail);
}
