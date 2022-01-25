package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerOpenSSORequest {
    @NotBlank
    private String connid;
    @NotBlank
    private String mobile;
    @Email
    private String email;
}
