# kafka.reactor

## Pre-requisites
 - Java 21
 - Docker
 - Maven

## Run tests
```bash
./mvnw clean test
```

## Build
```bash
./mvnw clean package -DskipTests=true
```

## UP - docker
```bash
./mvnw clean package -DskipTests=true && docker compose -f ./docker-compose-dependencies.yml -f ./docker-compose-app.yml up --build -d
```

## DOWN - docker
```bash
docker compose -f ./docker-compose-dependencies.yml -f ./docker-compose-app.yml down -v
```

## Check docker container health check log
```bash
docker inspect --format='{{json .State.Health}}' <container_name> | jq
```
