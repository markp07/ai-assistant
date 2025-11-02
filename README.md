# AI Assistant

AI Assistant is a Java project built with Maven and Docker. It integrates with the OpenAI model and external APIs to provide real-time railway information in the Netherlands and Belgium.

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Project Structure

The project is organized as follows:

1. **ai-assistant**: Contains the main application logic, including API integration and service layers.
2. **config**: Holds configuration files for the application.
3. **constant**: Defines constants used across the application.
4. **controller**: Contains REST controllers for handling API requests.
5. **service**: Implements the core business logic and integrations with external APIs.
6. **models**: Defines data models used throughout the application.
7. **exception**: Handles custom exceptions and error responses.
8. **mapper**: Provides mapping utilities between DTOs and domain models.

## Features

- **Natural Language Processing**: Understands and processes user inputs in natural language.
- **Real-Time Railway Information**: Fetches and displays real-time data for Dutch and Belgian railways.
- **Customizable**: Easily extendable to integrate with additional APIs or services.

## Docker Setup

### Dockerfile

The project includes a Dockerfile for containerization. The Dockerfile builds the application and packages it into a lightweight image. Key steps include:

1. **Base Image**: Uses an official OpenJDK image as the base.
   ```dockerfile
   FROM openjdk:11-jre-slim
   ```

2. **Build and Package**: Runs Maven to build the application and package it as a JAR file.
   ```dockerfile
   RUN ./mvnw -B -DskipTests clean package
   ```

3. **Copy Artifact**: Copies the built JAR file into the final image.
   ```dockerfile
   COPY --from=build /workspace/app/target/ai-assistant-*.jar /app.jar
   ```

4. **Run Command**: Specifies the default command to run the application.
   ```dockerfile
   CMD ["java", "-jar", "/app.jar"]
   ```

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/ai-assistant.git
   ```

2. Build the Docker image:
   ```bash
   docker build -t ai-assistant:local .
   ```

3. Run the container:
   ```bash
   docker run -e OPENAI_API_KEY="your-key" -p 8080:8080 --rm ai-assistant:local
   ```

## Usage

- Access the API documentation at `http://localhost:8080/swagger-ui.html`.
- Use the endpoints to fetch real-time railway information or interact with the AI assistant.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.