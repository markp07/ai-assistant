# AI Assistant Frontend

Modern web interface for the AI Assistant application, providing a responsive chat experience with multi-session support and real-time streaming responses.

## Description

The frontend is a Next.js-based single-page application that connects to the AI Assistant backend. It features a sidebar for managing multiple chat sessions, a main chat interface with streaming message support, and automatic authentication handling with JWT token refresh capabilities.

## Technologies

### Core Framework
- **Next.js 16.0.10**: React framework with App Router and server-side rendering
- **React 19.2.1**: Component-based UI library
- **TypeScript 5**: Static type checking

### Styling
- **Tailwind CSS 4**: Utility-first CSS framework
- **next-themes 0.4.6**: Theme management for light/dark mode

### Security
- **JWT Authentication**: Token-based authentication with HTTP-only cookies
- **Automatic Token Refresh**: Handles 401 responses by refreshing access tokens
- **Secure Cookie Storage**: Credentials stored in HTTP-only cookies to prevent XSS attacks

## Project Structure

```
frontend/
├── app/                    # Next.js App Router pages
│   ├── layout.tsx         # Root layout with providers
│   ├── page.tsx           # Main chat page
│   └── globals.css        # Global styles
├── components/            # React components
│   ├── Chat.tsx          # Main chat interface
│   ├── ChatMessage.tsx   # Individual message component
│   ├── Sidebar.tsx       # Session management sidebar
│   ├── AuthProvider.tsx  # Authentication context
│   ├── ThemeProvider.tsx # Theme context wrapper
│   ├── ThemeToggle.tsx   # Dark/light mode toggle
│   └── UserProfile.tsx   # User profile display
├── lib/                   # Utility libraries
│   ├── api.ts            # API client with auth handling
│   └── auth.ts           # Authentication utilities
└── types/                 # TypeScript type definitions
    └── chat.ts           # Chat-related types
```

## Getting Started

### Prerequisites

- Node.js 18 or higher
- Backend API running on port 7075 (or configured port)

### Installation

Install dependencies:

```bash
npm install
```

### Configuration

Create a `.env.local` file for local development:

```bash
NEXT_PUBLIC_API_URL=http://localhost:7075
```

**Environment Variables:**

| Variable | Description | Default |
|----------|-------------|---------|
| `NEXT_PUBLIC_API_URL` | Backend API base URL | `http://localhost:7075` |

### Development

Start the development server:

```bash
npm run dev
```

Access the application at http://localhost:3000

### Production Build

Build the application:

```bash
npm run build
```

Start the production server:

```bash
npm start
```

### Docker Deployment

#### Using Docker Compose

From the project root directory:

```bash
docker compose up -d
```

#### Standalone Container

```bash
# Build the application
npm run build

# Build Docker image
docker build -t ai-assistant-frontend .

# Run container
docker run -p 3000:3000 \
  -e NEXT_PUBLIC_API_URL=http://localhost:7075 \
  ai-assistant-frontend
```

## API Integration

The frontend integrates with the following backend endpoints:

### Session Management
- `GET /api/v1/sessions` - Retrieve all user sessions
- `POST /api/v1/sessions` - Create new session
- `GET /api/v1/sessions/{sessionId}` - Get session details
- `PUT /api/v1/sessions/{sessionId}` - Update session title
- `DELETE /api/v1/sessions/{sessionId}` - Delete session

### Messages
- `POST /api/v1/sessions/{sessionId}/messages/stream` - Send message and receive streaming response
- `GET /api/v1/sessions/{sessionId}/history` - Get message history

All requests include automatic authentication handling with token refresh on 401 responses.
