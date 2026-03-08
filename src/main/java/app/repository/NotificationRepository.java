package app.repository;

import app.model.Notification;
import app.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("""
            SELECT n FROM Notification AS n
            WHERE n.userId = :userId AND n.isDeleted = false
            ORDER BY n.createdOn DESC
            """)
    List<Notification> findAllByUserIdAndDeletedFalseOrderByCreatedOnDesc(UUID userId);

    @Query("""
            SELECT n FROM Notification AS n
            WHERE n.userId = :userId AND n.isDeleted = false AND n.status = :status
            """)
    List<Notification> findAllByUserIdAndStatusAndDeletedFalse(UUID userId, NotificationStatus status);
}
