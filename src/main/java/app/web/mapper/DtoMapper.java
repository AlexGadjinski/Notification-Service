package app.web.mapper;

import app.model.Notification;
import app.model.NotificationPreference;
import app.model.NotificationType;
import app.web.dto.NotificationPreferenceResponse;
import app.web.dto.NotificationResponse;
import app.web.dto.NotificationTypeRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static NotificationType toNotificationType(NotificationTypeRequest notificationTypeRequest) {

        return switch (notificationTypeRequest) {
            case EMAIL -> NotificationType.EMAIL;
        };
    }

    public static NotificationPreferenceResponse toNotificationPreferenceResponse(NotificationPreference notificationPreference) {

        return NotificationPreferenceResponse.builder()
                .id(notificationPreference.getId())
                .userId(notificationPreference.getUserId())
                .type(notificationPreference.getType())
                .notificationEnabled(notificationPreference.isEnabled())
                .contactInfo(notificationPreference.getContactInfo())
                .build();
    }

    public static NotificationResponse toNotificationResponse(Notification notification) {

        return NotificationResponse.builder()
                .subject(notification.getSubject())
                .createdOn(notification.getCreatedOn())
                .status(notification.getStatus())
                .type(notification.getType())
                .build();
    }
}
