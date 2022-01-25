package org.example.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppProperties {
    @Value("${wts.baseUrl}")
    private String wtsBaseUrl;
    @Value("${market.baseUrl}")
    private String marketBaseUrl;
    @Value("${security.access-token-validity-seconds}")
    private int tokenValiditySeconds;
    @Value("${app.limit.search}")
    private int limitSearch;
    @Value("${schedule.enable}")
    private Boolean isEnableSchedule;
    @Value("${trading.baseUrl}")
    private String tradingBaseUrl;
    @Value("${pine.baseUrl}")
    private String pineBaseUrl;
    @Value("${community.baseUrl}")
    private String communityBaseUrl;
    @Value("${spring.data.solr.host}")
    private String solrURL;
    @Value("${domain.org.image.avatar}")
    private String imageAvatarUrl;
    @Value("${dwh.baseUrl}")
    private String dwhBaseUrl;
    @Value("${referral.link.share}")
    private String linkShareUrl;
    @Value("${email.otp.link.verify}")
    private String emailOtpLinkVerify;
    @Value("${email.baseUrl}")
    private String emailBaseUrl;
    @Value("${notification.service.baseUrl}")
    private String notificationBaseUrl;

    public boolean isDisableSchedule() {
        return !isEnableSchedule;
    }
}
