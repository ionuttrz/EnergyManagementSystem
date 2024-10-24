# EnergyManagementSystem
The Energy Management System (EMS) is a robust application designed to manage users and their smart energy metering devices. It features a frontend interface and two core microservices—User Management and Device Management—catering to both administrators and clients.

## Overview
The primary goal of this repository is to implement the Monitoring and Communication Microservice, which will collect data from smart metering devices through a message broker middleware. This microservice processes energy consumption data, computes hourly usage, and stores results in its dedicated database.

Key Features
Message Broker Integration: Utilizes a message broker for seamless data collection from smart metering devices.
Smart Metering Device Simulator: A standalone desktop application that mimics smart meter functionality by reading energy data from a sensor.csv file and sending it to the message broker in real-time.
Hourly Consumption Calculation: Processes incoming measurements to compute total hourly energy consumption, storing results in the database.
User Notifications: Asynchronously alerts users via the web interface if their energy consumption exceeds predefined limits.
Event-Based Synchronization: Syncs with the Device Management Microservice through an event-driven architecture to update device changes efficiently.
Real-Time Communication: Includes a chat microservice for secure, real-time communication between users and administrators.
Authorization Component: Implements secure access control to ensure only authorized users can access the microservices.
Functional Requirements
Manage user and device information.
Compute and store hourly energy consumption.
Notify users of energy threshold breaches.
Enable secure communication between users and administrators.
Non-Functional Requirements
Scalability: Handle an increasing number of devices and users.
Reliability: Ensure robust message delivery and data integrity.
Security: Protect user data through encryption and secure authentication.
Performance: Optimize processing speed for real-time notifications.
