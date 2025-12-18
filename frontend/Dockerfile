FROM node:25-alpine AS builder
WORKDIR /app

ARG NEXT_PUBLIC_API_URL
ENV NEXT_PUBLIC_API_URL=$NEXT_PUBLIC_API_URL

COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Production image
FROM node:25-alpine as runner
WORKDIR /app
COPY --from=builder /app .
EXPOSE 3030
CMD ["npm", "start"]
