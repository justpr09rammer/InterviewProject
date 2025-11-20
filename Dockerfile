
FROM eclipse-temurin:17
WORKDIR /app

# Copy the JAR file
COPY build/libs/InterviewProject-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]