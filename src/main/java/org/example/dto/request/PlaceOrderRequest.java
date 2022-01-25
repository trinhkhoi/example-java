package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.utils.JSON;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaceOrderRequest {
    private String symbol;
    private Integer ordrUntprc;
    private Integer ordrQty;
    private String ordrTrdTp;
    private String buySelTp;
    private String oddOrdrYn;
    private Integer couponAmt = 0;
    private String type;
    private String token;
    private Boolean isPublic = false;

    public String toString() {
        try {
            return JSON.toPrettyJson(this);
        } catch (Exception ex) {
            StringBuilder buff = new StringBuilder();
            buff.append("symbol: ").append(symbol);
            buff.append("ordrUntprc: ").append(ordrUntprc);
            buff.append("ordrQty: ").append(ordrQty);
            buff.append("ordrTrdTp: ").append(ordrTrdTp);
            buff.append("buySelTp: ").append(buySelTp);
            buff.append("oddOrdrYn: ").append(oddOrdrYn);
            buff.append("couponAmt: ").append(couponAmt);
            buff.append("type: ").append(type);
            buff.append("token: ").append(token);
            buff.append("isPublic: ").append(isPublic);
            return buff.toString();
        }
    }
}
