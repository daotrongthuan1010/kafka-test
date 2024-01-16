FROM gradle:8.5.0-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build

FROM openjdk:17-jdk
WORKDIR /kafka-test
COPY --from=build /home/gradle/src/build/libs/Kafka-Test-0.0.1-SNAPSHOT-plain.jar /kafka-test/kafka-test.jar
EXPOSE 8081
CMD ["java", "-jar", "/kafka-test/kafka-test.jar"]