package nl.markpost.aiassistant.repository;

import nl.markpost.aiassistant.models.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ChatSession entities.
 */
@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, String> {

    List<ChatSession> findByUserIdOrderByUpdatedAtDesc(String userId);

    Optional<ChatSession> findByIdAndUserId(String id, String userId);

    @Modifying
    @Query("DELETE FROM ChatSession cs WHERE cs.id = :id AND cs.userId = :userId")
    void deleteByIdAndUserId(@Param("id") String id, @Param("userId") String userId);
}

