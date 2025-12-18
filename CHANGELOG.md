# Release Notes

## Version 1.0.0 - 2025-12-18

### 🎉 Major Release: Multi-Session Chat with Authentication

This is a major release introducing authentication, persistent storage, and multi-session support.

### ⚠️ Breaking Changes
- **Authentication Required**: All API endpoints now require JWT authentication
- **API Endpoints Changed**: Chat endpoints moved from `/api/v1/chat` to `/api/v1/sessions/{sessionId}/messages`
- **Database Required**: PostgreSQL is now required for persistent storage
- **New Environment Variables**: Several new required configuration variables (see Migration Guide)

### Added
- **JWT Authentication System**
  - Integration with external auth service (configurable via `AUTH_SERVICE_URL`)
  - Automatic token refresh when access token expires
  - JWT token verification using public key from auth service
  - User isolation - each user has their own chat sessions
  
- **PostgreSQL Database Integration**
  - Spring Data JPA with Hibernate
  - Two main entities: `ChatSession` and `ChatMessage`
  - Automatic schema creation and updates
  - Persistent storage for all chat history
  
- **Multi-Session Chat Support**
  - Create multiple chat sessions per user
  - Each session has its own title and message history
  - Session management: create, list, view, and delete sessions
  - Sidebar UI for easy navigation between sessions
  
- **Context-Aware AI Responses**
  - Automatically sends last 10 messages to OpenAI for context
  - Better conversation continuity and understanding
  - Configurable message history window
  
- **New Frontend Features**
  - `AuthProvider` component for authentication management
  - `Sidebar` component for session navigation
  - Automatic login redirect with callback URL
  - Token storage in localStorage
  - Responsive mobile-friendly sidebar
  
- **New API Endpoints**
  - `POST /api/v1/sessions` - Create new chat session
  - `GET /api/v1/sessions` - List all user's sessions
  - `GET /api/v1/sessions/{id}` - Get specific session
  - `GET /api/v1/sessions/{id}/history` - Get message history
  - `POST /api/v1/sessions/{id}/messages` - Send message
  - `DELETE /api/v1/sessions/{id}` - Delete session
  
- **New Backend Services**
  - `ChatSessionService` - Manages sessions and messages
  - `ChatSessionController` - REST API for session management
  - JPA repositories for database operations
  
- **Documentation**
  - `MIGRATION_MULTI_SESSION.md` - Complete migration guide
  - `.env.example` - Updated with all required variables
  - `dev-start.sh` - Local development setup script
  - Updated README with authentication and database setup

### Changed
- **Updated JwtAuthenticationFilter**
  - Now extracts user ID from JWT claims
  - Supports Authorization header with Bearer token
  - Better error messages for debugging
  
- **Updated Docker Compose**
  - Added PostgreSQL service
  - Database persistence with Docker volumes
  - Health checks for all services
  - Updated environment variable passing
  
- **Updated Frontend API Client**
  - Authentication headers on all requests
  - Automatic token refresh on 401 errors
  - New session-based API methods
  
- **Updated Chat Component**
  - Now requires session ID
  - Loads history from database
  - Better error handling

### Dependencies
- Added `spring-boot-starter-data-jpa`
- Added `postgresql` JDBC driver
- PostgreSQL 16 (Docker container)

### Migration
See `MIGRATION_MULTI_SESSION.md` for detailed migration instructions from previous versions.

### Environment Variables
New required variables:
- `DATABASE_URL` - PostgreSQL connection URL
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password
- `AUTH_SERVICE_URL` - External authentication service URL
- `JWT_PUBLIC_KEY_URL` - JWT public key endpoint
- `NEXT_PUBLIC_AUTH_URL` - Auth URL for frontend

### Known Issues
- None at this time

### Security
- All endpoints now require authentication
- User data isolation enforced at database level
- JWT token verification with RSA public key
- Secure token refresh flow

---

## Version 0.10.0 - 2025-12-18

### Added
- Unified environment configuration: Single `.env` file at project root for both backend and frontend
- `ENVIRONMENT_SETUP.md`: Comprehensive guide for environment configuration
- Dynamic CORS configuration: `ALLOWED_ORIGINS` environment variable with default fallback
- Frontend build-time configuration: `NEXT_PUBLIC_API_URL` build argument support in Docker

### Changed
- Updated `docker-compose.yml`: Pass environment variables to frontend as build args
- Updated `frontend/Dockerfile`: Multi-stage build with environment variable support
- Updated all deployment scripts (`build-and-up.sh`, `start.sh`, `restart.sh`, `stop.sh`) to load from root `.env`
- Updated `WebConfig.java`: Read CORS origins from application properties
- Updated `application.yaml`: Added configurable `cors.allowed-origins` property
- Updated `.env.example`: Merged backend and frontend environment variables with documentation
- Updated `README.md`: Documented unified environment configuration approach

### Improved
- Simplified deployment process with single configuration file
- Better separation of concerns between development and production configurations
- Enhanced documentation with troubleshooting guides and environment-specific examples

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