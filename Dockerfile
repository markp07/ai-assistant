# Use OpenJDK 21 with Alpine Linux as the base image for the build stage
FROM openjdk:21-jdk AS build
WORKDIR /workspace/app

# Copy Maven wrapper and project files to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY ai-assistant ai-assistant
COPY ai-assistant-api ai-assistant-api
COPY ai-assistant-hugging-face ai-assistant-hugging-face
COPY ai-assistant-common ai-assistant-common
COPY ai-assistant-ollama ai-assistant-ollama
COPY ai-assistant-openai ai-assistant-openai

# Build the project
RUN ./mvnw install

# Extract the project version to a file
RUN ./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout > version.txt

# Use OpenJDK 21 with a slim base image for the final stage
FROM openjdk:21-jdk-slim

# Set the maintainer label
LABEL maintainer="mark@markpost.nl"

# Define a volume for temporary files
VOLUME /tmp

# Expose port 8080
EXPOSE 8080

# Copy the JAR file from the build stage using the extracted version
COPY --from=build /workspace/app/ai-assistant/target/ai-assistant-$(cat /workspace/app/version.txt).jar app.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]