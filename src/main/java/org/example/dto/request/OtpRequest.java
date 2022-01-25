package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequest {

    private String code;

    private String otp;

    private String customerName;

    private String receiveEmail;

    private String linkVerify;
}
