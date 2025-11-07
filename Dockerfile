# Build
FROM amazoncorretto:21-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and configuration
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Pre-download dependencies (cache layer)
RUN ./gradlew dependencies --no-daemon || true

# Copy the application source code
COPY src ./src

# Build the Spring Boot jar (skip tests)
RUN ./gradlew clean bootJar -x test --no-daemon

# Run
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
