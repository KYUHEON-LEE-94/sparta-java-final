FROM openjdk:11-jdk-slim
WORKDIR /app

COPY build/libs/*.jar gateway-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gateway-service.jar"]