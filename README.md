# AI Assistant

AI Assistant is a full-stack application with a Java backend and Next.js frontend. It integrates with the OpenAI model to provide an interactive chat experience.

## Table of Contents

- [Project Structure](#project-structure)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

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

- **Natural Language Processing**: Understands and processes user inputs in natural language.
- **Interactive Chat Interface**: Modern, responsive UI that works across all devices.
- **Theme Support**: Toggle between light and dark modes.
- **Real-time Communication**: Seamless interaction with the AI backend.
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

### Docker Deployment (Recommended)

The easiest way to run both backend and frontend together:

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/ai-assistant.git
   cd ai-assistant
   ```

2. Set your OpenAI API key in `.env` file:
   ```bash
   echo "OPENAI_API_KEY=your-key-here" > .env
   ```

3. Build the frontend application:
   ```bash
   ./build-frontend.sh
   ```
   
   Or manually:
   ```bash
   cd frontend && npm install && npm run build && cd ..
   ```

4. Start all services with Docker Compose:
   ```bash
   docker compose up -d --build
   ```

5. Access the application:
   - **Frontend**: http://localhost:12502
   - **Backend API**: http://localhost:12501
   - **API Documentation**: http://localhost:12501/swagger-ui.html

6. Stop all services:
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
   cp .env.local.example .env.local
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

### API Endpoints

- `POST /api/chat` - Send a message to the AI assistant
- `DELETE /api/chat` - Clear chat history

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.