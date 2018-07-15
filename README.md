# The Challenge Project

## About

We would like to have a restful API for our statistics. The main use case for our API is to
calculate realtime statistic from the last 60 seconds. There will be two APIs, one of them is
called every time a transaction is made. It is also the sole input of this rest API. The other one
returns the statistic based of the transactions of the last 60 seconds.

## Prerequisites

- Java 1.8+
- Maven 3.3+

## Running the project
- To compile and run the test : `mvn clean install`
- To run the application locally : `mvn clean spring-boot:run` 

## Already Implemented :
- The API have to be threadsafe with concurrent requests
- The API have to function properly, with proper result
-  The project should be buildable, and tests should also complete successfully. e.g. If
   maven is used, then mvn clean install should complete successfully.
- The API should be able to deal with time discrepancy, which means, at any point of time,
we could receive a transaction which have a timestamp of the past
- Make sure to send the case in memory solution without database (including in-memory
database)
-  Endpoints have to execute in constant time and memory (O(1))
## Improvements
- Handling the concurrency with a smart lock.
