package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class UpsertNotificationPreference {

    @NotNull
    private UUID userId;

    private boolean notificationEnabled;

    @NotNull
    private NotificationTypeRequest type;

    @NotBlank
    private String contactInfo;
}
