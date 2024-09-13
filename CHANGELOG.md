# Release Notes

## Version 0.0.1 - 2023-10-05

### New Features

- **Chat API**: Introduced the `ChatController` with the `/v1/chat` endpoint to handle chat
  requests.
- **Base Path Configuration**: Configured the base path for the API to `/api` using
  `application.properties` or `application.yml`.
- **OpenAPI Integration**: Added OpenAPI annotations to the `ChatApi` interface for better API
  documentation.

### Enhancements

- **Dependency Management**: Updated `pom.xml` to include necessary dependencies for Spring Boot,
  Lombok, and MapStruct.
- **Executable Script**: Added a `postChat.sh` script to facilitate testing the chat endpoint using
  `curl`.

### Bug Fixes

- N/A

### Known Issues

- **Mapper Implementation**: The `chatPost` method in `ChatController` has a TODO for creating a
  mapper. This needs to be implemented in future releases.

### Miscellaneous

- **Project Structure**: Organized the project into modules (`ai-assistant-api` and `ai-assistant`)
  for better maintainability.