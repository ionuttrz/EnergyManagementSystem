package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDTO;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDifference;
import com.SistemeInformaticeDistribuite.energyManagementSystem.model.Monitoring;
import com.SistemeInformaticeDistribuite.energyManagementSystem.repository.IMonitoringRepository;
import com.SistemeInformaticeDistribuite.energyManagementSystem.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@AllArgsConstructor
public class MonitoringServiceImpl implements IMonitoringService {

    private final IMonitoringRepository iMonitoringRepository;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public ResponseEntity<Monitoring> addMonitoring(MonitoringDTO monitoringDTO) {
        Monitoring monitoring = new Monitoring();
        monitoring.setDeviceId(monitoringDTO.getDeviceId());
        monitoring.setTimestamp(monitoringDTO.getTimestamp());
        monitoring.setMeasurementValue(monitoringDTO.getMeasurementValue());

        monitoring = iMonitoringRepository.save(monitoring);

        return ResponseEntity.ok(monitoring);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    @Override
    public ResponseEntity<?> getMonitoredValues(HttpServletRequest request, Integer deviceId) throws IllegalAccessException {
        String token = extractTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String userRole = jwtUtils.getUserRoleFromJwtToken(token);
        if (!userRole.contains("CLIENT")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        List<Monitoring> monitoringList = iMonitoringRepository.findAllByDeviceIdOrderByTimestampDesc(deviceId);
        return ResponseEntity.ok(monitoringList);
    }

    @Override
    public ResponseEntity<List<MonitoringDifference>> getDifValues(Integer deviceId) {
        List<MonitoringDifference> diff = getSensorDifference(deviceId);
        return ResponseEntity.ok(diff);
    }

    private List<MonitoringDifference> getSensorDifference(Integer deviceId) {
        List<Monitoring> monitoringList = iMonitoringRepository.findAllByDeviceIdOrderByTimestampDesc(deviceId);

        // Calculate the difference between consecutive values and order by timestamp
        List<MonitoringDifference> differences = IntStream.range(0, monitoringList.size() - 1)
                .mapToObj(i -> {
                    Monitoring current = monitoringList.get(i);
                    Monitoring prev = monitoringList.get(i + 1);
                    double difference = current.getMeasurementValue() - prev.getMeasurementValue();
                    if (difference < 0) {
                        difference = 0;
                    }
                    String formattedDifference = String.format("%.3f", difference);

                    return new MonitoringDifference(current.getTimestamp(), Double.parseDouble(formattedDifference));
                })
                .sorted(Comparator.comparing(MonitoringDifference::getTimestamp))
                .collect(Collectors.toList());
        return differences;
    }

    public ResponseEntity<Map<String, Double>> getSumValues(Integer deviceId) {
        /*List<MonitoringDifference> diff = getSensorDifference(deviceId);

        Map<String, Double> sumByMinute = diff.stream()
                .collect(Collectors.groupingBy(
                        monitoringDifference -> formatMinute(monitoringDifference.getTimestamp()),
                        Collectors.summingDouble(MonitoringDifference::getDifference)
                ));

        Map<String, Double> sortedSumByMinute = sumByMinute.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        return ResponseEntity.ok(sortedSumByMinute);*/
        List<MonitoringDifference> diff = getSensorDifference(deviceId);

        Map<String, Double> sumByMinute = diff.stream()
                .collect(Collectors.groupingBy(
                        monitoringDifference -> formatMinute(monitoringDifference.getTimestamp()),
                        Collectors.summingDouble(MonitoringDifference::getDifference)
                ));

        List<Map.Entry<String, Double>> sortedEntries = sumByMinute.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        int size = sortedEntries.size();
        int startIndex = Math.max(0, size - 24); // Starting index for the last 24 entries

        List<Map.Entry<String, Double>> last24Entries = sortedEntries.subList(startIndex, size);

        Map<String, Double> result = last24Entries.stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        return ResponseEntity.ok(result);
    }

    private String formatMinute(Date timestamp) {
        SimpleDateFormat minuteFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        return minuteFormat.format(timestamp);
    }
}
