package app.service;

import app.model.NotificationPreference;
import app.repository.NotificationPreferenceRepository;
import app.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSender;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {

    @Captor
    private ArgumentCaptor<NotificationPreference> captor;

    @Mock
    private NotificationPreferenceRepository preferenceRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private MailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void givenMissingNotificationPreferenceInDatabase_whenUpdateNotificationPreference_thenExceptionIsThrown() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        when(preferenceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NullPointerException.class, () -> notificationService.updateNotificationPreference(userId, true));
    }

    @Test
    void givenExistingNotificationPreferenceInDatabase_whenUpdateNotificationPreference_thenIsEnabledIsChanged() {
        // GIVEN
        UUID userId = UUID.randomUUID();
        NotificationPreference preference = NotificationPreference.builder()
                .isEnabled(false)
                .build();
        when(preferenceRepository.findByUserId(userId)).thenReturn(Optional.of(preference));

        // WHEN
        notificationService.updateNotificationPreference(userId, true);

        // THEN
        verify(preferenceRepository, times(1)).save(captor.capture());
        NotificationPreference result = captor.getValue();

        assertTrue(result.isEnabled());
    }
}
