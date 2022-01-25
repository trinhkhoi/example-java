package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class ClaimCouponRequest {
    private String trandate;
    private String channel;
    private String cif;
    private String couponid;
    private String couponcode;
    private Long value;
}
