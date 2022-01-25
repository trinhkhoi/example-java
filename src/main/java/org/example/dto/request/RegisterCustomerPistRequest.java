package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.common.utils.JSON;
import org.common.validation.CustomerName;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCustomerPistRequest {
    @CustomerName
    private String fullName;
    @CustomerName
    private String displayName;
    @NotBlank
    private String phone;
    private String email;
    private String refCode;
    private Integer step;
    private String username;

    public String getPhone() {
        return phone.replace(" ", "");
    }

    @Override
    public String toString() {
        try {
            return JSON.toPrettyJson(this);
        } catch (com.fasterxml.jackson.core.JsonProcessingException jpe) {
            LoggerFactory.getLogger(RegisterCustomerPistRequest.class).error("error with toString", jpe);
            return super.toString();
        }
    }
}
