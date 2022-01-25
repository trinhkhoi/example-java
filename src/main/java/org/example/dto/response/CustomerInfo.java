package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerInfo {
    private Long customerId;
    private String name;
    private String displayName;
    private String phoneNumber;
    private String avatar;
    private String email;
}
