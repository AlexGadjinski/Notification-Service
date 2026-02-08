package app.service;

import app.model.Notification;
import app.model.NotificationPreference;
import app.model.NotificationStatus;
import app.model.NotificationType;
import app.repository.NotificationPreferenceRepository;
import app.repository.NotificationRepository;
import app.web.dto.NotificationRequest;
import app.web.dto.UpsertNotificationPreference;
import app.web.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {
    private final NotificationPreferenceRepository preferenceRepository;
    private final NotificationRepository notificationRepository;
    private final MailSender mailSender;

    public NotificationService(NotificationPreferenceRepository preferenceRepository, NotificationRepository notificationRepository, MailSender mailSender) {
        this.preferenceRepository = preferenceRepository;
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    public NotificationPreference upsertPreference(UpsertNotificationPreference dto) {

        Optional<NotificationPreference> optionalPreference = this.preferenceRepository.findByUserId(dto.getUserId());
        LocalDateTime now = LocalDateTime.now();

        if (optionalPreference.isPresent()) {

            NotificationPreference preference = optionalPreference.get().toBuilder()
                    .type(DtoMapper.toNotificationType(dto.getType()))
                    .isEnabled(dto.isNotificationEnabled())
                    .contactInfo(dto.getContactInfo())
                    .updatedOn(now)
                    .build();

            return this.preferenceRepository.save(preference);
        }

        NotificationPreference preference = NotificationPreference.builder()
                .userId(dto.getUserId())
                .type(DtoMapper.toNotificationType(dto.getType()))
                .isEnabled(dto.isNotificationEnabled())
                .contactInfo(dto.getContactInfo())
                .createdOn(now)
                .updatedOn(now)
                .build();

        return this.preferenceRepository.save(preference);
    }

    public NotificationPreference getPreferenceByUserId(UUID userId) {

        return this.preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("Notification preference for user id [%s] does not exist.".formatted(userId)));

    }

    public Notification sendNotification(NotificationRequest notificationRequest) {

        UUID userId = notificationRequest.getUserId();
        NotificationPreference preference = getPreferenceByUserId(userId);
        if (!preference.isEnabled()) {
            throw new IllegalArgumentException("User with id [%s] has disabled notifications.".formatted(userId));
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(preference.getContactInfo());
        message.setSubject(notificationRequest.getSubject());
        message.setText(notificationRequest.getBody());

        Notification notification = Notification.builder()
                .subject(notificationRequest.getSubject())
                .body(notificationRequest.getBody())
                .createdOn(LocalDateTime.now())
                .type(NotificationType.EMAIL)
                .userId(userId)
                .isDeleted(false)
                .build();

        try {
            this.mailSender.send(message);
            notification.setStatus(NotificationStatus.SUCCEEDED);

        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            log.warn("There was an issue sending an email to [%s] due to [%s]".formatted(preference.getContactInfo(), e.getMessage()));
        }

        return this.notificationRepository.save(notification);
    }

    public List<Notification> getNotificationHistory(UUID userId) {

        return this.notificationRepository.findAllByUserIdAndDeletedFalseOrderByCreatedOnDesc(userId);
    }

    public NotificationPreference updateNotificationPreference(UUID userId, boolean isEnabled) {

        NotificationPreference preference = getPreferenceByUserId(userId).toBuilder()
                .isEnabled(isEnabled)
                .build();
        return this.preferenceRepository.save(preference);
    }
}
