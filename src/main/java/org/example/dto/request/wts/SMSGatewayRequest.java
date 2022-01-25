package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSGatewayRequest {

    @NotBlank
    private String mobile;
    @NotBlank
    private String content;
    private String commandCode;
    private String feeType;
    private String msgType;
    private String sendFrom;
}
