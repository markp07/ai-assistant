# AI Assistant

AI Assistant is a Java project built with Maven and Docker. It integrates with the OpenAI model and the NS API to provide real-time railway information in the Netherlands.

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Project Structure

The project is organized into two modules:

1. **ai-assistant-api**: This module holds the API specification and generates the API interfaces and models.
2. **ai-assistant**: This module contains the main application with a single endpoint.

The parent POM file is `ai-assistant-parent`.

## Features

- **Natural Language Processing**: Understands and processes user inputs in natural language.
- **Task Automation**: Automates repetitive tasks to save time and increase efficiency.
- **Machine Learning**: Utilizes machine learning models to continuously improve performance.
- **Customizable**: Easily customizable to suit different use cases and industries.

## Installation

To install and run the AI Assistant locally, follow these steps:

1. **Clone the repository**
    ```sh
    git clone https://github.com/mark3970/ai-assistant.git
    cd ai-assistant
    ```

2. **Build the project using Maven**
    ```sh
    mvn clean install
    ```

3. **Run the application**
    ```sh
    cd ai-assistant
    mvn spring-boot:run
    ```

## Usage

Once the application is running, you can interact with the AI Assistant through the `/api/v1/chat` endpoint. This is a POST endpoint that accepts a model with a `chat` field.

Example CURL command:
```sh
curl -X POST http://localhost:8080/api/v1/chat -H "Content-Type: application/json" -d '{"chat": "Hello, AI Assistant!"}'
```

Refer to the [User Guide](docs/user_guide.md) for detailed instructions on how to use the features of the AI Assistant.

## Continuous Integration

This project uses GitHub Actions for Continuous Integration. The workflow is defined in `.github/workflows/maven.yml` and includes the following steps:

- **Checkout code**: Uses `actions/checkout@v4` to checkout the code.
- **Setup Maven**: Uses `s4u/setup-maven-action@v1.7.0` to set up Maven with Java 21 and Maven 3.9.9.
- **Build with Maven**: Runs `mvn -B package --file pom.xml` to build the project.
- **Run tests and generate JaCoCo report**: Runs `mvn test jacoco:report` to execute tests and generate a JaCoCo coverage report.
- **Check code style with Google Java Format**: Runs `mvn com.spotify.fmt:fmt-maven-plugin:check` to check code formatting.
- **Display Dependency Updates**: Runs `mvn versions:display-dependency-updates` to display available dependency updates.
- **Display Plugin Updates**: Runs `mvn versions:display-plugin-updates` to display available plugin updates.

## Contributing

We welcome contributions from the community! To contribute to this project, follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add new feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Create a Pull Request.

Please refer to the [Contributing Guide](CONTRIBUTING.md) for more details.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.