package nl.markpost.aiassistant.config;

import org.springframework.context.annotation.Configuration;

/**
 * Assistant configuration. The Assistant is now built dynamically per request in AiProviderService
 * to support runtime switching between AI providers (OpenAI and Ollama).
 */
@Configuration
public class AssistantConfig {}
