package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.validation.CustomerName;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileUpdateRequest {
    @CustomerName
    private String fullName;
    @CustomerName
    private String displayName;
    private String bio;
    private String position;
    private String email;
    private String address;
    private String avatar;
}
