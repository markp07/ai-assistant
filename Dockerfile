# Use OpenJDK 25 JRE with a slim base image
FROM eclipse-temurin:25-jre-jammy

# Set the maintainer label
LABEL maintainer="mark@markpost.nl"

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Define a volume for temporary files
VOLUME /tmp

# Expose port 7075
EXPOSE 7075

# Copy the pre-built JAR file from the target directory
# Note: Run 'mvn clean package' before building this Docker image
COPY target/ai-assistant-*.jar /app.jar

# Set environment variables
ENV OPENAI_API_KEY=${OPENAI_API_KEY}
ENV NS_API_KEY=${NS_API_KEY}

# Set the entry point to run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]