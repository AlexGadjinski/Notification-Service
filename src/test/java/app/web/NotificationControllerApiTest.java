package app.web;

import app.TestSetup;
import app.service.NotificationService;
import app.web.dto.NotificationTypeRequest;
import app.web.dto.UpsertNotificationPreference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToNotificationPreference_happyPath() throws Exception {

        MockHttpServletRequestBuilder request = get("/api/v1/notifications/preferences")
                .param("userId", UUID.randomUUID().toString());

        when(notificationService.getPreferenceByUserId(any())).thenReturn(TestSetup.aNotificationPreference());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("notificationEnabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());
    }

    @Test
    void postRequestWithBodyToNotificationPreference_returns201AndCorrectDtoStructure() throws Exception {

        UpsertNotificationPreference requestDto = UpsertNotificationPreference.builder()
                .userId(UUID.randomUUID())
                .notificationEnabled(true)
                .type(NotificationTypeRequest.EMAIL)
                .contactInfo("user123@abv.bg")
                .build();
        MockHttpServletRequestBuilder request = post("/api/v1/notifications/preferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(requestDto));

        when(notificationService.upsertPreference(any())).thenReturn(TestSetup.aNotificationPreference());

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("userId").isNotEmpty())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("notificationEnabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty());
    }
}
