# Use OpenJDK 21 with Alpine Linux as the base image for the build stage
FROM maven:3.9.11-eclipse-temurin-25-alpine AS build
WORKDIR /workspace/app

# Copy Maven wrapper and project files to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Ensure the Maven wrapper is executable and build the project (skip tests for faster builds)
RUN chmod +x mvnw \
 && MAVEN_CONFIG= ./mvnw -B clean package -DskipDependencyCheck=true

# Use OpenJDK 21 with a slim base image for the final stage
FROM eclipse-temurin:25-jre-jammy

# Set the maintainer label
LABEL maintainer="mark@markpost.nl"

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Define a volume for temporary files
VOLUME /tmp

# Expose port 7075
EXPOSE 7075

# Copy the JAR file from the build stage (build working dir is /workspace/app)
COPY --from=build /workspace/app/target/ai-assistant-*.jar /app.jar

# Set environment variables
ENV OPENAI_API_KEY=${OPENAI_API_KEY}
ENV NS_API_KEY=${NS_API_KEY}

# Set the entry point to run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]