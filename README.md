# AI Assistant

AI Assistant is a full-stack application with a Java backend and Next.js frontend. It integrates with the OpenAI model to provide an interactive chat experience.

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Environment Configuration](#environment-variables)
- [Contributing](#contributing)
- [License](#license)

## Additional Documentation

- **[Environment Setup Guide](ENVIRONMENT_SETUP.md)**: Comprehensive guide for configuring environment variables
- **[Migration Guide](MIGRATION.md)**: Instructions for migrating from old configuration system
- **[Changelog](CHANGELOG.md)**: Release notes and version history

## Project Structure

The project is organized as follows:

### Backend (Java/Spring Boot)
1. **ai-assistant**: Contains the main application logic, including API integration and service layers.
2. **config**: Holds configuration files for the application.
3. **constant**: Defines constants used across the application.
4. **controller**: Contains REST controllers for handling API requests.
5. **service**: Implements the core business logic and integrations with external APIs.
6. **models**: Defines data models used throughout the application.
7. **exception**: Handles custom exceptions and error responses.
8. **mapper**: Provides mapping utilities between DTOs and domain models.

### Frontend (Next.js)
The `frontend` folder contains a responsive Next.js application with:
- Modern React 19 and Next.js 16 architecture
- TypeScript for type safety
- Tailwind CSS for styling
- Light and dark mode support
- Responsive design for desktop, tablet, and mobile devices

See [frontend/README.md](frontend/README.md) for detailed frontend documentation.

## Features

- **Authentication**: Secure JWT-based authentication with external auth service integration
  - Login via configurable auth service
  - Automatic token refresh handling
  - Session-based access control
- **Multi-Session Chat**: Create and manage multiple chat conversations
  - Sidebar for easy navigation between chats
  - Persistent chat history stored in PostgreSQL
  - Delete unwanted conversations
- **Context-Aware AI**: Sends last 10 messages to OpenAI for better context understanding
- **Natural Language Processing**: Understands and processes user inputs in natural language
- **Interactive Chat Interface**: Modern, responsive UI that works across all devices
- **Theme Support**: Toggle between light and dark modes
- **Real-time Communication**: Seamless interaction with the AI backend
- **PostgreSQL Database**: Reliable storage for chat sessions and message history
- **Customizable**: Easily extendable to integrate with additional APIs or services

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

### Docker Deployment (Recommended)

The easiest way to run both backend and frontend together:

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/ai-assistant.git
   cd ai-assistant
   ```

2. Create `.env` file from the example and configure your environment:
   ```bash
   cp .env.example .env
   ```
   
   Then edit `.env` and set your values (minimum required):
   ```bash
   OPENAI_API_KEY=your-key-here
   ALLOWED_ORIGINS=http://localhost:7070
   NEXT_PUBLIC_API_URL=http://localhost:7075
   ```
   
   **Note:** The `.env` file at the project root contains all environment variables for both backend and frontend services.

3. Build and start all services:
   ```bash
   ./build-and-up.sh
   ```
   
   Or use the start script (includes Docker cleanup):
   ```bash
   ./start.sh
   ```

4. Access the application:
   - **Frontend**: http://localhost:12502
   - **Backend API**: http://localhost:12501
   - **API Documentation**: http://localhost:12501/swagger-ui.html

5. Stop all services:
   ```bash
   ./stop.sh
   ```
   
   Or manually:
   ```bash
   docker compose down
   ```

### Backend Setup (Manual)

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
   docker run -e OPENAI_API_KEY="your-key" -p 7075:7075 --rm ai-assistant:local
   ```

### Frontend Setup (Manual)

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. (Optional) Create `.env.local` file:
   ```bash
   cp .env.example .env.local
   ```

4. Start the development server:
   ```bash
   npm run dev
   ```

5. Open [http://localhost:3000](http://localhost:3000) in your browser.

For production builds:
```bash
npm run build
npm start
```

### Frontend Docker Deployment (Standalone)

To build and run only the frontend container:

```bash
cd frontend
docker build -t ai-assistant-frontend:latest .
docker run -p 3000:3000 -e NEXT_PUBLIC_API_URL=http://localhost:7075 ai-assistant-frontend:latest
```

## Usage

### With Docker Compose
- **Frontend**: Access the chat interface at `http://localhost:12502`
- **Backend API**: Access the API documentation at `http://localhost:12501/swagger-ui.html`

### Development Mode (without Docker)
- **Frontend**: Access the chat interface at `http://localhost:3000`
- **Backend API**: Access the API documentation at `http://localhost:7075/swagger-ui.html`

## Authentication

The application uses JWT-based authentication with an external auth service. Users must log in to access the chat functionality.

### Authentication Flow

1. User visits the frontend application
2. If not authenticated, user is redirected to the login page with a callback URL
3. After successful login, the auth service returns `access_token` and `refresh_token`
4. Frontend stores tokens and uses them for API requests
5. When access token expires (401 response), frontend automatically refreshes using refresh token
6. If refresh fails, user is redirected back to login

### Configuration

Set the auth service URL in your `.env` file:
```bash
AUTH_SERVICE_URL=http://localhost:7080
JWT_PUBLIC_KEY_URL=http://localhost:7080/api/auth/v1/public-key
```

## Database

The application uses PostgreSQL to store chat sessions and message history. Each user can have multiple chat sessions with persistent history.

### Database Schema

- **chat_sessions**: Stores individual chat sessions
  - `id`: UUID primary key
  - `user_id`: User identifier from JWT token
  - `title`: Session title
  - `created_at`: Creation timestamp
  - `updated_at`: Last update timestamp

- **chat_messages**: Stores individual messages within sessions
  - `id`: UUID primary key
  - `session_id`: Foreign key to chat_sessions
  - `role`: Either "user" or "assistant"
  - `content`: Message text
  - `timestamp`: Message timestamp

### Environment Variables

The application uses a single `.env` file at the project root for both backend and frontend configuration.

#### Backend Variables

| Variable             | Description                                  | Default Value                                      | Required |
|----------------------|----------------------------------------------|----------------------------------------------------|----------|
| `OPENAI_API_KEY`     | Your OpenAI API key for AI functionality     | -                                                  | Yes      |
| `DATABASE_URL`       | PostgreSQL connection URL                    | `jdbc:postgresql://localhost:5432/ai_assistant`    | No       |
| `DATABASE_USERNAME`  | PostgreSQL username                          | `postgres`                                         | No       |
| `DATABASE_PASSWORD`  | PostgreSQL password                          | `postgres`                                         | No       |
| `AUTH_SERVICE_URL`   | External authentication service URL          | `http://localhost:7080`                        | No       |
| `JWT_PUBLIC_KEY_URL` | URL to fetch JWT verification public key     | `http://localhost:7080/api/auth/v1/public-key` | No       |
| `ALLOWED_ORIGINS`    | Comma-separated list of allowed CORS origins | `http://localhost:7070`                            | No       |

#### Frontend Variables

| Variable               | Description                      | Default Value           | Required |
|------------------------|----------------------------------|-------------------------|----------|
| `NEXT_PUBLIC_API_URL`  | Backend API URL for the frontend | `http://localhost:7075` | No       |
| `NEXT_PUBLIC_AUTH_URL` | Authentication service URL       | `http://localhost:7080  | No       |

Example `.env` file:
```bash
# OpenAI Configuration
OPENAI_API_KEY=sk-your-key-here

# Database Configuration
DATABASE_URL=jdbc:postgresql://localhost:5432/ai_assistant
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres

# Authentication
AUTH_SERVICE_URL=http://localhost:7080
JWT_PUBLIC_KEY_URL=http://localhost:7080/api/auth/v1/public-key

# Backend CORS
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:7070

# Frontend Configuration
NEXT_PUBLIC_API_URL=http://localhost:7075
NEXT_PUBLIC_AUTH_URL=http://localhost:7080
```

**Note:** For Docker deployments, all environment variables are loaded from the root `.env` file and passed to the appropriate services.

### API Endpoints

#### Chat Sessions
- `POST /api/v1/sessions` - Create a new chat session
- `GET /api/v1/sessions` - Get all user's chat sessions
- `GET /api/v1/sessions/{sessionId}` - Get a specific session with messages
- `GET /api/v1/sessions/{sessionId}/history` - Get message history for a session
- `POST /api/v1/sessions/{sessionId}/messages` - Send a message in a session
- `DELETE /api/v1/sessions/{sessionId}` - Delete a chat session

All endpoints require JWT authentication via Authorization header: `Bearer <access_token>`

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.