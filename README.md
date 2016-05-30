# Synopsis

Implemented a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

# Installation
##### Cloud Service
This service is also up and running on AWS Cloud.
###### http://ec2-23-20-219-115.compute-1.amazonaws.com:8080/

##### Commands
 - Build and Package
```
mvn clean package
```
 - Run Test
```
mvn test
```
 - Run App
```
mvn spring-boot:run
```
##### Curl Commands
 - Get Avilable Seats without level prefrence
```
curl -X GET --header 'Accept: application/json' --header 'api-key: WALMART' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/availableSeats'
```
 - Get Avilable Seats with level prefrence  
```
curl -X GET --header 'Accept: application/json' --header 'api-key: WALMART' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/availableSeats?levelId=1'
```
 - Hold Seats without level prefrence
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'api-key: WALMART' -d '{
  "numSeats": 10,
  "customerEmail": "test@test.com"
}' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/holdSeats
```
 - Hold Seats with Min Level Prefrence
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'api-key: WALMART' -d '{
  "numSeats": 10,
  "minLevel":1,
  "customerEmail": "test@test.com"
}' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/holdSeats'
```
 - Hold Seats with Max Level Prefrence
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'api-key: WALMART' -d '{
  "numSeats": 10,
  "maxLevel":2,
  "customerEmail": "test@test.com"
}' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/holdSeats'
```
 - Hold Seats with Min and Max Level Prefrence
```
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'api-key: WALMART' -d '{
  "numSeats": 10,
"minLevel":1,
  "maxLevel":2,
  "customerEmail": "test@test.com"
}' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/holdSeats'
```
 - Reserve Seats
```
curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' --header 'api-key: WALMART' -d '{
  "seatHoldId": 832489,
  "customerEmail": "test@test.com"
}' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/1/v1/reserveSeats'
```
 - Reset Entire DB
```
curl -X DELETE --header 'Accept: application/json' --header 'api-key: WALMART' 'http://<HostName>:8080/api/walmart/onlineServices/vanue/{vanueId}/v1/resetService'
```
##### Cloud Mongo URI
```
https://mlab.com/databases/ticketservice
```
##### RabbitMQ Cloud URI
```
https://jellyfish.rmq.cloudamqp.com/#/queues
```
##### Assumptions
 - Java 8 is setup.
 - Maven is setup.
 - Maven has a access to Central Reposetry.
 - Firewall should have access to below Cloud hosts for mongo and rabbit.
```
s036789.mlab.com:36789  
jellyfish.rmq.cloudamqp.com
```
# Design Overview
Designed the solution using the Spring Boot, Jersey, MongoDB and RabbitMQ.
  - Spring Boot: Spring Boot is used for creating stand-alone application.
  - Jersey: JAX-RS Reference Implementation.
  - MongoDB: Used Mongo to store Documents for total available seats, Holding the seats and Reserving held seats.

> This is the Cloud mongo cluster runnig at https://mlab.com/databases/ticketservice.

  - RabbitMQ: This used to maintain life cycle of held message and achieve the fault tolerence.Queue will hold the message for 2 mins and then it will trigger the application to remove the held seats if those seats are not resevered yet.

> This is the MQ as a Cloud service running at https://jellyfish.rmq.cloudamqp.com/#/queues.  

# Fault Tolerence
This System is very robust and caan handle the multu point failover.
 - Application Server Fault: Mongo will persist the data and MQ will hold the message for held ticket and wait for the system to be up.Once it will be up this will delete the held ticket and update the Total avaiable seats if seats are not booked and if it is then it will drop the message.
 - Mongo Fault: Used Mongo as a service running on the AWS EC2 and it will persist the message.
 - MQ Fault: Used MQ as a service running on EC2 and it is persistence System.

# Scalibility
 - This system can be easly scale.System is based on Venue ID so for scalling this app to more Venue is just required to load venue details to mongo DB.
 - This is the distributed system so each server can be easly scalled based on the performance hit.  

# Mongo Sample Documents
Venue Details:  
```
{        
    "level_id": 1,  // Level on Each Venue   
    "vanueId": 0,  // Venue ID  
    "levelName": "Orchestra",// Level Name  
    "price": "$100",// Price for that level  
    "totalAvailableSeats": 1150  // Total available seats for this level  
}
```
```
Ticket Details:
{
    "_id": 344714, // Ticket Hold ID  
    "bookinfRefNo": 743900, // Booking Ref No(0 for Hold Ticket)  
    "customerEmail": "abc@abc.com", // Email ID of Customer  
    "seatsDetails": [// List of the seats which held/booked  
        {  
            "noOfSeat": 100,  
            "levelId": 1,  
            "levenName": "Orchestra"  
        }  
    ],  
    "vanueId": 1,  
    "noOfSeats": 100,// Total No Of Seats requested  
    "status": "BOOKED"// HOLD or BOOKED  
}  
```
# MQ Sample Document
Ticket Details:  
```
{  
    "_id": 344714, // Ticket Hold ID  
    "bookinfRefNo": 743900, // Booking Ref No 
    "customerEmail": "abc@abc.com", // Email ID of Customer  
    "seatsDetails": [// List of the seats which held/booked  
        {  
            "noOfSeat": 100,  
            "levelId": 1,  
            "levenName": "Orchestra"  
        }  
    ],  
    "vanueId": 1,  
    "noOfSeats": 100,// Total No Of Seats requested  
    "status": "HOLD"// HOLD  
}
```
