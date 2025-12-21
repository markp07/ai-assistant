package nl.markpost.aiassistant.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
  private String id;
  private String role;
  private String content;
  private LocalDateTime timestamp;
}
