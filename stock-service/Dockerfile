FROM openjdk11:jdk-11.0.2.9-slim
WORKDIR /app
COPY build/libs/stock-service-0.0.1-SNAPSHOT.jar stock-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "stock-service-0.0.1-SNAPSHOT.jar"]