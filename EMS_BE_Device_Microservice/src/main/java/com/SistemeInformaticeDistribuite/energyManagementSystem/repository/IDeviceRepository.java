package com.SistemeInformaticeDistribuite.energyManagementSystem.repository;

import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByUserEmail(@Param("user_email") String userEmail);

    Optional<Device> findById(@Param("device_id") Integer deviceId);

    Boolean existsByUserEmail(String userEmail);
}
