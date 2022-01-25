package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCoreData {

    private String cif;
    private String couponid;
    private String couponcode;
    private String channel;
    private Long value;
    private String status;
    private String trandate;
    private String expiredate;

    /*
        status definition from WTS:
        1 - active
        2 - pending transfer to money
        3 - deactive/ or expired
     */
    public String getStatusStr() {
        if (StringUtils.isBlank(status)) {
            return "NOT_JOIN";
        } else if ("1".equalsIgnoreCase(getStatus())) {
            return "CLAIM";
        } else if ("2".equalsIgnoreCase(getStatus())) {
            return "CLAIM_PENDING";
        } else if ("3".equalsIgnoreCase(getStatus())) {
            return "CLAIMED";
        }
        return "NOT_JOIN";
    }
}
