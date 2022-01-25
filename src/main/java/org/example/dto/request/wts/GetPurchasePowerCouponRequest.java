package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class GetPurchasePowerCouponRequest extends GetPurchasePowerRequest{
    private String couponAmt = "0";

}
