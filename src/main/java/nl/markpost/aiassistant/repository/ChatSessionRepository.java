package nl.markpost.aiassistant.repository;
}
    void deleteByIdAndUserId(String id, String userId);

    Optional<ChatSession> findByIdAndUserId(String id, String userId);

    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(String userId);

public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {
@Repository

import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import nl.markpost.aiassistant.models.entity.ChatSession;


