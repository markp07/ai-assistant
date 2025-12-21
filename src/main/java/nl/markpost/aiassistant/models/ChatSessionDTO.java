package nl.markpost.aiassistant.models;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionDTO {
  private String id;
  private String title;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private List<MessageDTO> messages;
}
