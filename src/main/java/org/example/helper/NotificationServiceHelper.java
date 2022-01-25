package org.example.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.common.dto.request.notification.PushNotificationBasicRequest;
import org.common.dto.response.notification.PushNotificationResponse;
import org.common.utils.Constant;
import org.example.util.AppProperties;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceHelper {

    @Qualifier("restTemplateSystem")
    private final RestTemplate restTemplateSystem;

    private final AppProperties appProperties;

    public void sendPush(List<PushNotificationBasicRequest> request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Authorization", Constant.TOKEN_SYSTEM);

            HttpEntity<List<PushNotificationBasicRequest>> entity = new HttpEntity<>(request, headers);

            String url = appProperties.getNotificationBaseUrl() + "/system/notification/push";
            PushNotificationResponse response = restTemplateSystem.postForObject(url, entity, PushNotificationResponse.class);
            assert response != null;
            log.info("Send push notification response: code={}, message={} ", response.getStatus(), response.getMessage());
        } catch (Exception ex) {
            log.error("Error during send push notification ", ex);
        }
    }
}
