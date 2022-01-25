package org.example.dto.request.pine;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VerifyOtpRequest {

    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String otp;
    private Boolean isVerifyFromPist = Boolean.TRUE;

    public String getPhoneNumber() {
        return phoneNumber.replace(" ", "");
    }
}
