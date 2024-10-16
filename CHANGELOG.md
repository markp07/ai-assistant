# Release Notes

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