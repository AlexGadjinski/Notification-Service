package app;

import app.model.NotificationPreference;
import app.model.NotificationType;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

@UtilityClass
public class TestSetup {

    public static NotificationPreference aNotificationPreference() {
        LocalDateTime now = LocalDateTime.now();

        return NotificationPreference.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .type(NotificationType.EMAIL)
                .isEnabled(true)
                .contactInfo("user123@abv.bg")
                .createdOn(now)
                .updatedOn(now)
                .build();
    }
}
