package nl.markpost.aiassistant.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nl.markpost.aiassistant.api.controller.MessagesApi;
import nl.markpost.aiassistant.api.model.Message;
import nl.markpost.aiassistant.api.model.SendMessageRequest;
import nl.markpost.aiassistant.mapper.SessionApiMapper;
import nl.markpost.aiassistant.models.MessageDTO;
import nl.markpost.aiassistant.service.ChatMessagesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

/**
 * Controller for managing chat sessions. Implements the generated SessionsApi interface from
 * OpenAPI specification.
 */
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatMessagesController implements MessagesApi {

  private final ChatMessagesService chatMessagesService;
  private final SessionApiMapper sessionApiMapper;

  /**
   * Retrieves the message history for a specific chat session.
   *
   * @param sessionId the ID of the chat session
   * @return a ResponseEntity containing a list of Messages
   */
  @Override
  public ResponseEntity<List<Message>> getSessionHistory(String sessionId) {
    String userId = getUserId();
    List<MessageDTO> historyDTO = chatMessagesService.getSessionHistory(sessionId, userId);
    List<Message> response = sessionApiMapper.toApiMessageList(historyDTO);
    return ResponseEntity.ok(response);
  }

  /**
   * Sends a message in a chat session.
   *
   * @param sessionId the ID of the chat session
   * @param sendMessageRequest the request body containing the message
   * @return a ResponseEntity containing the sent Message
   */
  @Override
  public ResponseEntity<Message> sendMessage(
      String sessionId, SendMessageRequest sendMessageRequest) {
    String userId = getUserId();
    String messageContent = sendMessageRequest.getMessage();
    MessageDTO messageDTO = chatMessagesService.sendMessage(sessionId, userId, messageContent);
    Message response = sessionApiMapper.toApiModel(messageDTO);
    return ResponseEntity.ok(response);
  }

  /**
   * Sends a message in a chat session with streaming response.
   *
   * @param sessionId the ID of the chat session
   * @param sendMessageRequest the request body containing the message
   * @return a Flux of Server-Sent Events containing the streaming response
   */
  @PostMapping(
      value = "/sessions/{sessionId}/messages/stream",
      produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent<String>> sendMessageStream(
      @PathVariable String sessionId, @RequestBody SendMessageRequest sendMessageRequest) {
    String userId = getUserId();
    String messageContent = sendMessageRequest.getMessage();
    return chatMessagesService
        .sendMessageStream(sessionId, userId, messageContent)
        .map(token -> ServerSentEvent.<String>builder().data(token).build());
  }

  /**
   * Helper method to extract user ID from the security context.
   *
   * @return the authenticated user's ID
   */
  private String getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }
}
