# Vehicle GeoLocation Streaming Java App

This code base is a Spring Boot Application with gRPC Streaming for Vehicle Geolocation
It has the following services:
Develop two Spring Boot applications:
- producer-service: Generates and broadcasts random geolocations for multiple vehicles over a configurable channel.
- consumer-service: Allows users to open a gRPC stream and receive the last known geolocation of a vehicle every 2 seconds, configurable by the backend.

## Keywords
*Keywords*: gRPC, Spring Boot 3, Java 21


### 1. Required requirements

- Install Java 21
- Install Maven 3.9.9

### 2. Running the applications

#### 2.1. Proto file

* generate of proto classes

    ```bash
    cd proto
    mvn clean compile
    ```

#### 2.2. GRPC Client consumer GRPC Server with java

##### 2.2.1. Steps:

* install dependencies

    ```bash
    mvn clean install
    ```

* run consumer-service

    ```bash
    cd consumer-service
    mvn spring-boot:run
    ```

* run producer-service

    ```bash
    cd producer-service
    mvn spring-boot:run
    ```
  
##### 2.2.2. GRPC consumer-service web-api Documentation:

* Example curl:

    ```bash
    curl --location 'http://localhost:8081/vehicle/geolocation' \
    --header 'Content-Type: application/json' \
    --header 'Accept: application/json' \
    --data '{
        "ids": ["1","2","3"]
    }'
    ```
   **NOTE**: Status code 200 with response body:
    ```
   [
    {
        "1": []
    },
    {
        "2": []
    },
    {
        "3": []
    }
    ]
    ```

### 3. Generating test coverage

* from the main dir run the following command

    ```bash
    mvn clean test site
    ```

Open the following file to see the results directly in a browser : target/site/jacoco/index.html