# Warehouse Service

## Overview

The Warehouse Service collects environmental data from sensors via UDP and publishes it to a Kafka topic. This service is a part of a reactive system designed to monitor warehouse conditions.

## Prerequisites

1. **Java 17 or higher**: Ensure that you have Java Development Kit (JDK) 17 or a later version installed on your machine.

2. **Apache Kafka**: You need to have Kafka running locally and create a `sensor-data-topic` topic .

3. **Maven 3.9.6 or higher**: For building the project make sure you have installed the maven.

4. **Docker (Optional)**: For running Kafka and Zookeeper via Docker if you prefer not to install them directly.

## Setup

1. **Clone the Repository**:

    ```sh
    git clone https://github.com/your-repo/warehouse-service.git
    cd warehouse-service
    ```

2. **Install Dependencies**:

   For Maven:

    ```sh
    mvn clean install
    ```

3. **Configure Kafka**:

   Ensure that Kafka is running on `localhost:9092`. You can use Docker to run Kafka and Zookeeper:

    ```sh
    docker-compose up -d
    ```

   Docker Compose file (`docker-compose.yml`):

    ```yaml
      version: '2.1'

      services:
         zookeeper:
            image: wurstmeister/zookeeper:latest
            ports:
            - "2181:2181"
      
         kafka:
            image: wurstmeister/kafka:latest
            ports:
            - "9092:9092"
            expose:
              - "9093"
            environment:
              KAFKA_ADVERTISED_LISTENERS: INSIDE://kafka:9093,OUTSIDE://localhost:9092
              KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
              KAFKA_LISTENERS: INSIDE://0.0.0.0:9093,OUTSIDE://0.0.0.0:9092
              KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
              KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
              KAFKA_CREATE_TOPICS: "my-topic:1:1"
            volumes:
              - /var/run/docker.sock:/var/run/docker.sock
      ```
     Create kafka topic `sensor-data-topic` by below command:

     ```sh
      docker exec -it <your-container-id> /opt/kafka/bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic sensor-data-topic
      ```
 
## Running the Service Locally

1. **Start the Service**:

   For Maven:

    ```sh
    mvn spring-boot:run
    ```

2. **Test UDP Listener**:

   Use a tool like `netcat` to send UDP messages to test the listener:

    ```sh
    echo "sensor_id=t1;value=38" | nc -u -w1 localhost 3344
    ```
    ```sh
    echo "sensor_id=h1;value=51" | nc -u -w1 localhost 3355
    ```
  
3. **Verify**:

   Check the application logs for messages about receiving and publishing data.

## Common Issues

- **Kafka Connection Issues**: Ensure Kafka is running and configured correctly. Check the logs for any errors related to Kafka connectivity.
- **UDP Listener**: Ensure the UDP port (3344 and 3355) are not blocked by any firewall or other services.
