package org.example.constant;

import lombok.Getter;

@Getter
public enum RegisterStep {
    VALID_PHONE_NUMBER(1),
    VERIFY_OTP(2),
    VALID_INFORMATION_USER(3),
    VALID_USERNAME(4)
    ;

    private final int step;

    private RegisterStep(int step) {
        this.step = step;
    }

}
