# Energy State Data Analysis (ESDA)

ESDA is a component-oriented reference and test application for use in performance benchmarks and tests. This community case study emulates a message-based system for analyzing energy state data.

## Getting Started
We provide images of the components via Docker Hub. They can be found here:
* https://hub.docker.com/r/kitsdq/esda-analysis
* https://hub.docker.com/r/kitsdq/esda-aggregate
* https://hub.docker.com/r/kitsdq/esda-importer

To deploy the system locally, run:

```sh
docker-compose -f docker-compose_kafka.yml up

# Wait until the containers do not output much log data anymore (startup is over).
# In another shell:
docker-compose -f docker-compose_clients.yml up

# Test
docker run --rm -it --network esda_esdanet alpine /bin/sh
```

Inside the container, run:
```sh
apk add curl

curl "importer:8080/add/abc/30"
curl "importer:8080/add/abc/30"
curl "importer:8080/add/abc/30"
```


## Building Yourself
If you want to build the containers yourself, run:

```sh
# Git
git clone git@github.com:kit-sdq/esda.git
cd .\esda

# Build
mvn clean package

# Execute
docker-compose -f docker-compose_kafka.yml up

# Wait until the containers do not output much log data anymore (startup is over).
# In another shell:
docker-compose -f docker-compose_clients_build.yml up --build

# Test
docker run --rm -it --network esda_esdanet alpine /bin/sh
```

Inside the container, run:
```sh
apk add curl

curl "importer:8080/add/abc/30"
curl "importer:8080/add/abc/30"
curl "importer:8080/add/abc/30"
```

## Reference Documentation

For further reference, please consider the following sections:

* [Kafka Docker](https://wurstmeister.github.io/kafka-docker)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#boot-features-kafka)
* [Apache Kafka Streams Support](https://docs.spring.io/spring-kafka/docs/current/reference/html/_reference.html#kafka-streams)
* [Apache Kafka Streams Binding Capabilities of Spring Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/#_kafka_streams_binding_capabilities_of_spring_cloud_stream)
