FROM openjdk:11-jdk-slim
WORKDIR /app

COPY build/libs/*.jar eureka-server.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]