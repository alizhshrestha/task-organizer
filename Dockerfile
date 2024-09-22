FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/task-organizer-0.0.1.jar /app/task-organizer-0.0.1.jar
EXPOSE 8080
CMD ["java", "-jar", "task-organizer-0.0.1.jar"]