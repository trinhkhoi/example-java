package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPurchasePowerData {
    private Long acntPp;
    private Long acntTopupPp;
    private Long couponTopupPp;
    private Long ordrAbleQty;
    private Long bankAvailAmt;
    private Double fee;
    private Long ppOnCash;
}
