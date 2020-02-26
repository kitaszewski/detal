# detal
CRM application for local internet provider. Customer database, device and configuration managment. Client module for smsgate.

Maven project
Spring Boot, Spring Data, Spring Security
JPA with MySQL
REST endpoints
Application consumes Google API for Google Sheets, Mikrotik API to sending configuration sets to the router (NAT addresses and static DHCP binding) 
Telnet connection with GPON OLT for manage a configuration sets for customers' devices in the fiber optic network.
Application consume API from smsgate (on my github profile) by restTemplate. It gives a possibility for sending SMS to customers.
Contracts with customers are generated by Apach POI
Frontend powered by Bootstrap, Thymeleaf, JS
Log4j2 is logging to /var/log/
Lombok

application.properties and other sensetive files are removed from this public repository.
