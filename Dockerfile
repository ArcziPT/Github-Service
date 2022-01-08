FROM openjdk:11-jdk-slim
COPY target/github-service-0.0.1-SNAPSHOT.jar /app/github-service.jar
ENTRYPOINT ["java","-jar","/app/github-service.jar"]