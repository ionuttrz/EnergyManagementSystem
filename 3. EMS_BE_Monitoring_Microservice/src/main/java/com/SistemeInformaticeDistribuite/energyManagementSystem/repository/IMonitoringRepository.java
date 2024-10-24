package com.SistemeInformaticeDistribuite.energyManagementSystem.repository;

import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMonitoringRepository extends JpaRepository<Monitoring, Integer>  {
    Optional<Monitoring> findById(@Param("monitoring_id") Integer monitoringId);
    List<Monitoring> findAllByDeviceIdOrderByTimestampDesc(Integer deviceId);
}
