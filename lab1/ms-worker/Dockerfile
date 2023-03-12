FROM openjdk:17.0.1-jdk-slim
COPY /build/libs/ms-worker.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]