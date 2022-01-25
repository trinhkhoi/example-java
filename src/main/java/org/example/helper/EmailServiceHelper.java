package org.example.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;
import org.common.dto.response.ResponseDTO;
import org.common.exception.BusinessException;
import org.example.dto.request.OtpRequest;
import org.example.util.AppProperties;


@Validated
@Service("emailServiceHelper")
public class EmailServiceHelper {

    private static Logger logger = LoggerFactory.getLogger(EmailServiceHelper.class);
    @Autowired
    @Qualifier("restTemplateSystem")
    private RestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;

    /**
     * Send otp email verify
     *
     * @return
     * @throws BusinessException
     */
    public Boolean sendOtpEmailVerify(OtpRequest request) {
        try {
            ResponseDTO response = restTemplate.postForObject(appProperties.getEmailBaseUrl() + "/system/email/otp", request, ResponseDTO.class);
            return Boolean.TRUE;
        } catch (Exception ex) {
            return Boolean.FALSE;
        }
    }
}
