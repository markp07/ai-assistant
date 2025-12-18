package nl.markpost.aiassistant.repository;

import nl.markpost.aiassistant.models.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    @Query("SELECT m FROM ChatMessage m WHERE m.chatSession.id = :sessionId ORDER BY m.timestamp DESC")
    List<ChatMessage> findLastMessagesBySessionId(String sessionId, Pageable pageable);

    List<ChatMessage> findByChatSessionIdOrderByTimestampAsc(String sessionId);
}

