# AI Assistant Frontend

A responsive chat interface for the AI Assistant built with Next.js, React, and Tailwind CSS.

## Features

- 💬 Real-time chat interface
- 🌓 Light and dark mode support
- 📱 Responsive design (desktop, tablet, and mobile)
- 🎨 Modern UI with Tailwind CSS
- ⚡ Built with Next.js 15 and React 19

## Getting Started

### Prerequisites

- Node.js 18+ installed
- AI Assistant backend running on port 7075

### Installation

1. Install dependencies:

```bash
npm install
```

2. Create a `.env.local` file (optional):

```bash
cp .env.local.example .env.local
```

Edit the `.env.local` file if you need to change the API URL (default is `http://localhost:7075`).

### Development

Run the development server:

```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) with your browser to see the application.

### Building for Production

Build the application:

```bash
npm run build
```

Start the production server:

```bash
npm start
```

## Architecture

- **Next.js 16**: React framework with App Router
- **TypeScript**: Type-safe development
- **Tailwind CSS**: Utility-first CSS framework
- **next-themes**: Theme management for light/dark mode

## API Integration

The frontend communicates with the backend API at:
- `POST /api/chat` - Send a message to the AI
- `DELETE /api/chat` - Clear chat history

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `NEXT_PUBLIC_API_URL` | Backend API URL | `http://localhost:7075` |

## Responsive Design

The application is fully responsive and works seamlessly on:
- 📱 Mobile phones (320px+)
- 📱 Tablets (768px+)
- 💻 Desktops (1024px+)

## Building the Application

The application uses Next.js 16 with Turbopack for fast builds.
