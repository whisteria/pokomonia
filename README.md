# Pokomonia

Simple Rest project to learn a bit of [micronaut](https://docs.micronaut.io).
The learning process can be followed in the git commits.

# Overview
Simple Java 17 project (so a jdk 17 is needed), driven by `gradle` i.e. one can use for example:

```shell
# runs the tests
./gradlew test

# starts the server on port 8080 i.e. access via http://localhost:8080
./gradlew run
```

As the project is quite simple orchestration logic resides in the single controller which calls the clients directly.
There are exception handlers to control the http status returned in case of errors.

## Docker
Micronout has a gradle target to create a docker file and build the image:

```shell
# this create the docker file $project/build/docker/main/Dockerfile and builds the image
./gradlew dockerBuild

# use this for example to run the image
docker run -p 8080:8080 pokomonia

```

# Further Steps

## Productionize
- use configuration rather than hard coded values (like urls)
- add metrics
- add logging

## FurtherLearning
- include OpenApi endpoint (swagger or rapidoc)
- improve testing (for example learn how to inject the http clients)
- investigate json path libraries
- move to kotlin or use reactive programming in java, `Flow`
