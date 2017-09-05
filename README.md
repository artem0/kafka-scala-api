# Kafka sample
1. Spark streaming and Kafka sample
2. Akka streams and Kafka sample 
3. Akka actors and Kafka sample
4. Kafka SSL configuration sample for brokers/clients/zookeeper and script for generating signed certificates

## Installation
Install Kafka in docker

    docker run --rm -it \
	    -p 2181:2181 -p 3030:3030 -p 7081:8081 \
	    -p 7082:8082 -p 7083:8083 -p 9092:9092 \
	    -e ADV_HOST=127.0.0.1 \
	    landoop/fast-data-dev

Management of Kafka is available on <http://localhost:3030>

# Access to cli in container
    
    docker run --rm -it --net=host landoop/fast-data-dev bash
    
    kafka-topics --zookeeper 127.0.0.1:2181 --create --topic sample_topic --partitions 3 --replication-factor 1
    
    kafka-console-producer --broker-list 127.0.0.1:9092 --topic sample_topic
    
    kafka-topics --zookeeper 127.0.0.1:2181 --describe topic sample_topic

# Rest proxy

Rest proxy is available on <http://localhost:7082/topics/sample_topic> via curl e.g.:

    curl -i -X POST -H "Content-Type: application/vnd.kafka.avro.v1+json" --data '{
     "value_schema": "{\"type\": \"record\", \"name\": \"User\", \"fields\": [{\"name\": \"username\", \"type\": \"string\"}]}",
     "records": [
     {"value": {"username": "testUser"}},
     {"value": {"username": "testUser2"}}
     ]
    }' \
    http://localhost:7082/topics/sample_topic

# Launching 

    sbt project ...
    sbt runMain ...