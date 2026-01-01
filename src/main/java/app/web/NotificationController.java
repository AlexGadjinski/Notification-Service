package app.web;

import app.model.Notification;
import app.model.NotificationPreference;
import app.service.NotificationService;
import app.web.dto.NotificationPreferenceResponse;
import app.web.dto.NotificationRequest;
import app.web.dto.NotificationResponse;
import app.web.dto.UpsertNotificationPreference;
import app.web.mapper.DtoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> upsertNotificationPreference(@RequestBody UpsertNotificationPreference upsertNotificationPreference) {

        NotificationPreference notificationPreference = this.notificationService.upsertPreference(upsertNotificationPreference);
        NotificationPreferenceResponse preferenceResponse = DtoMapper.toNotificationPreferenceResponse(notificationPreference);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(preferenceResponse);
    }

    @GetMapping("/preferences")
    public ResponseEntity<NotificationPreferenceResponse> getNotificationPreference(@RequestParam(name = "userId") UUID userId) {

        NotificationPreference notificationPreference = this.notificationService.getPreferenceByUserId(userId);
        NotificationPreferenceResponse preferenceResponse = DtoMapper.toNotificationPreferenceResponse(notificationPreference);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(preferenceResponse);
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest notificationRequest) {

        Notification notification = this.notificationService.sendNotification(notificationRequest);

        NotificationResponse notificationResponse = DtoMapper.toNotificationResponse(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationResponse);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotificationHistory(@RequestParam(name = "userId") UUID userId) {

        List<NotificationResponse> notificationResponses = this.notificationService.getNotificationHistory(userId).stream()
                .map(DtoMapper::toNotificationResponse)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(notificationResponses);
    }
}
