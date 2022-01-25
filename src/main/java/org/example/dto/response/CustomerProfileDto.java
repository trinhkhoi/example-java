package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.jpa.NativeQueryEntity;
import org.example.util.Constant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NativeQueryEntity
public class CustomerProfileDto {
    private Long id;
    private String username;
    private String cif;
    private String guid;
    private String fcmToken;
    private String vsd;
    private String accountNo;
    private String subAccount;
    private String authDef;
    private Boolean firstLogin;
    private String phone;
    private String email;
    private Constant.DeviceType deviceType;
    private String name;
    private String displayName;
    private String avatar;
    private String caption;
    private Boolean isKol;
    private Boolean isFeatureProfile;
    private String fullDes;
    private Double kolPoint;
    private Boolean hasProAccount;
    private Boolean hasSyncContact;
    private String position;
    private String address;
    private String dob;
    private String identityCardNo;
    private String contactAddress;
    private String gender;
    private String openDate;
    private String custStat;
    private String acntStat;
    private Integer totalFollowing;
    private Integer totalFollower;
}
