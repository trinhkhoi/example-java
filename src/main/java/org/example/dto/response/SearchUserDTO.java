package org.example.dto.response;

import lombok.Builder;
import lombok.Data;
import org.common.utils.Constant;

@Data
@Builder
public class SearchUserDTO {
    private Long id;
    private String username;
    private String cif;
    private String phone;
    private String email;
    private Constant.CustStat custStat;
    private Constant.AcntStat acntStat;
    private String name;
    private String displayName;
    private String avatar;
    private String caption;
    private Boolean isKol;
    private Boolean isFeatureProfile;
}
