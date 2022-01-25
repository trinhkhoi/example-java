package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class GetPurchasePowerRequest {
    private String cif;
    private String acntNo;
    private String subAcntNo;
    private String symbol;
    private String ordrUntprc;

}
