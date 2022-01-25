package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.utils.DataUtil;
import org.common.validation.CustomerName;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterCustomerTempRequest {
    @CustomerName(allowEmpty = false)
    private String fullName;
    @CustomerName
    private String displayName;
    @NotBlank
    private String phone;
    @NotBlank
    private String email;
    @NotBlank
    private String userName;
    @NotBlank
    private String identityNo ;
    @NotBlank
    private String issueDate;
    @NotBlank
    private String issueBy;
    private String city;
    private String address;
    private String dob;
    private Boolean gender;
    private Boolean isAcceptCondition;

    public String getPhone() {
        return DataUtil.safeToStringWithEmptySpace(phone);
    }

    public String getUserName() {
        return DataUtil.safeToStringWithEmptySpace(userName);
    }
}
