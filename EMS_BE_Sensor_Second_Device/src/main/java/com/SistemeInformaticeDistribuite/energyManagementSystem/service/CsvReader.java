package com.SistemeInformaticeDistribuite.energyManagementSystem.service;

import com.SistemeInformaticeDistribuite.energyManagementSystem.controller.MessageJsonController;
import com.SistemeInformaticeDistribuite.energyManagementSystem.dto.MonitoringDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CsvReader {

    @Autowired
    private MessageJsonController messageJsonController;

    @Autowired
    public CsvReader(MessageJsonController messageJsonController) {
        this.messageJsonController = messageJsonController;
    }

    public void startCsvReading (String csvFilePath) {
        Thread readerThread = new Thread(() -> {
            readAndSendCsv(csvFilePath);
        });
        readerThread.start();
    }

    private static MonitoringDTO convertCsvToMonitoringDTO(int userInput, String csvLine) {
        MonitoringDTO monitoringDTO = new MonitoringDTO();
        monitoringDTO.setDeviceId(userInput);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        monitoringDTO.setTimestamp(date);
        Float measuredValue = Float.parseFloat(csvLine);
        monitoringDTO.setMeasurementValue(measuredValue);
        return monitoringDTO;
    }

    private void readAndSendCsv(String filePath) {

        System.out.print("Enter device id: ");
        Scanner scanner = new Scanner(System.in);
        int userInput = 0;
        if (scanner.hasNextInt()) {
            userInput = scanner.nextInt();
            System.out.println("You entered: " + userInput);
        } else {
            System.out.println("Invalid input. Please enter an integer.");
        }
        scanner.close();
        while(true){
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    MonitoringDTO monitoringDTO = convertCsvToMonitoringDTO(userInput, line);
                    //System.out.println(monitoringDTO);
                    messageJsonController.sendJsonMessage(monitoringDTO);
                    Thread.sleep(10000);  //10 s
                }
                System.out.println("-------- End of CSV Data --------");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

