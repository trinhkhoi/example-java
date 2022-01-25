package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class SaveCouponCoreRequest {
    private String trandate;
    private String channel = "07";
    private String iotype = "1";
    private String cif;
    private String couponid;
    private String couponcode;
    private String value;
    private String expiredate;

}
