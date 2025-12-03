# Release Notes

## Version 1.0.0 - 2025-12-03

### Migration to Java 25 and Spring Boot 4

#### Updated
- Migrated from Java 21 to Java 25
- Upgraded Spring Boot from 3.5.7 to 4.0.0
- Upgraded AssertJ from 3.27.6 to 4.0.0-M1
- Replaced `spring-boot-starter-aop` with `spring-boot-starter-aspectj` (Spring Boot 4 breaking change)
- Removed `ServletComponentScan` annotation (removed in Spring Boot 4)

#### Maintained Latest Versions
- Spring Cloud: 2025.1.0
- Lombok: 1.18.42
- MapStruct: 1.6.3
- Langchain4j: 1.9.1
- Hibernate Validator: 9.1.0.Final
- Jackson: 2.20.1
- OpenFeign: 13.6
- SpringDoc OpenAPI: 3.0.0
- Mockito: 5.20.0
- JaCoCo: 0.8.14
- Maven Compiler Plugin: 3.14.1
- OpenAPI Generator Maven Plugin: 7.17.0

#### Technical Notes
- Build and tests verified with Java 25
- All code formatting checks pass
- No security vulnerabilities detected
- Application successfully packages as executable JAR

## Version 0.9.0 - 2025-11-15

## Updated
- Refactored project structure to remove all modules since there is so little code. 
- Updated README.md to reflect new project structure and usage instructions.
- Updated Dockerfile to build the single-module project.

## Removed
- Removed all multi-module related files and configurations.
- Remove train-information code since it is moved to a separate project.
- Removed train-information API spec code.

## Version 0.0.8 - 2025-11-02

### Added
- Dependabot configuration with grouped dependency updates for automated dependency management

### Updated
- Bumped langchain4j dependency from version 0.35.0 to 1.8.0
- Bumped org.springframework.cloud:spring-cloud-dependencies to latest version
- Bumped org.mockito:mockito-junit-jupiter from version 4.0.0 to 5.20.0
- Bumped org.hibernate.validator:hibernate-validator to latest version
- Updated maven-dependencies group with 15 dependency updates
- Updated openjdk in the docker-dependencies group
- Updated github-actions-dependencies group with 2 updates
- Updated README.md to include Belgium/BeRail API information and ai-assistant-external-api module
- Updated CHANGELOG.md with comprehensive release notes

## Version 0.0.7 - 2023-10-16

### Updated
- Dockerfile to build project and build docker image
- Docker compose file to run the application
- ReadMe file updated instructions to run application


## Version 0.0.6 - 2023-10-13

### Added
- DTO models
- Added mappers

### Updated
- Module names
- Used DTO models in application
- Updated test cases
- Updated docker files
- Updated shell scripts


## Version 0.0.5 - 2023-10-05

## Added
- Added mvn wrapper
- Add shell file

## Updated
- Update Dockerfile

## Version 0.0.4 - 2023-09-27

## Updated

- Split project into multiple modules for better organization and maintainability: ai-assistant,
  ai-assistant-api, ai-assistant-interfaces, ai-assistent-openai, ai-assistent-huggingface,
  ai-assistent-ollama, and ai-assistant-parent.

## Version 0.0.3 - 2023-09-14

### Added

- Added OpenAI API integration to the `ChatService` to process user inputs and generate responses.

## Version 0.0.2 - 2023-09-13

### Added

- **Continuous Integration**: Set up GitHub Actions for Continuous Integration, including steps for
  building, testing, and checking code style.
- **Dependency Updates**: Added `versions-maven-plugin` to check for updates to dependencies and
  plugins.

### Updated

- **README Update**: Updated `README.md` to include instructions for Continuous Integration setup
  and usage.

## Version 0.0.1 - 2024-09-12

### Added

- **Chat API**: Introduced the `ChatController` with the `/v1/chat` endpoint to handle chat
  requests.
- **Base Path Configuration**: Configured the base path for the API to `/api` using
  `application.properties` or `application.yml`.
- **OpenAPI Integration**: Added OpenAPI annotations to the `ChatApi` interface for better API
  documentation.

### Updated

- **Dependency Management**: Updated `pom.xml` to include necessary dependencies for Spring Boot,
  Lombok, and MapStruct.
- **Executable Script**: Added a `postChat.sh` script to facilitate testing the chat endpoint using
  `curl`.

### Miscellaneous

- **Project Structure**: Organized the project into modules (`ai-assistant-api` and `ai-assistant`)
  for better maintainability.