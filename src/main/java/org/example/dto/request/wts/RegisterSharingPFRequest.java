package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterSharingPFRequest {
    private String cif ;
    private String acntNo;
    private String subAcntNo;
    private String publicYN;    // "Y" or "N"
}
