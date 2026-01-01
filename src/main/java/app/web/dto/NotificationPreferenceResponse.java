package app.web.dto;

import app.model.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class NotificationPreferenceResponse {

    private UUID id;

    private UUID userId;

    private NotificationType type;

    private boolean notificationEnabled;

    private String contactInfo;
}
