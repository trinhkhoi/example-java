package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.common.security.Secure;
import org.common.security.UserDetails;
import org.common.utils.JSON;
import org.example.util.Constant;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collections;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
@Where(clause = "deleted_at IS NULL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends BaseEntity implements UserDetails {

    private static Logger logger = LoggerFactory.getLogger(Customer.class);

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "cif", columnDefinition = "varchar(255)")
    private String cif;

    @Column(name = "guid", columnDefinition = "varchar(255)")
    private String guid;

    @Column(name = "fcm_token", columnDefinition = "text")
    private String fcmToken;

    @Column(name = "vsd", columnDefinition = "varchar(255)")
    private String vsd;

    @Column(name = "account_no ", columnDefinition = "varchar(255)")
    private String accountNo;

    @Column(name = "sub_account", columnDefinition = "varchar(255)")
    private String subAccount;

    @Column(name = "auth_def", columnDefinition = "varchar(255)")
    private String authDef;

    @Column(name = "first_login", columnDefinition = "bit(1)")
    private Boolean firstLogin;

    @Column(name = "phone", columnDefinition = "varchar(255)")
    private String phone;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "customer_state", columnDefinition = "enum('PRO','NEW')")
    @Enumerated(EnumType.STRING)
    private org.common.utils.Constant.CustStat custStat;

    @Column(name = "acnt_state", columnDefinition = "enum('ACTIVE','VSD_PENDING','VSD_REJECTED','CLOSED','PENDING_TO_CLOSE')")
    @Enumerated(EnumType.STRING)
    private org.common.utils.Constant.AcntStat acntStat;

    @Column(name = "device_type", columnDefinition = "enum('MOBILE_ANDROID', 'MOBILE_IOS', 'WEB')")
    @Enumerated(EnumType.STRING)
    private Constant.DeviceType deviceType;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "display_name", columnDefinition = "varchar(255)")
    private String displayName;

    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Column(name = "cover_image", columnDefinition = "text")
    private String coverImage;

    @Column(name = "caption", columnDefinition = "text")
    private String caption;

    @Column(name = "full_des", columnDefinition = "text")
    private String fullDes;

    @Column(name = "is_kol", columnDefinition = "bit(1)")
    private Boolean isKol;

    @Column(name = "is_feature_profile", columnDefinition = "bit(1)")
    private Boolean isFeatureProfile;

    @Column(name = "kol_point", columnDefinition = "Decimal(10,2)")
    private Double kolPoint;

    @Column(name = "has_sync_contact", columnDefinition = "bit(1)")
    private Boolean hasSyncContact;

    @Column(name = "position", columnDefinition = "varchar(255)")
    private String position;

    @Column(name = "address", columnDefinition = "varchar(255)")
    private String address;

    @Column(name = "dob", columnDefinition = "date")
    private LocalDate dob;

    @Column(name = "identity_card_no", columnDefinition = "varchar(255)")
    private String identityCardNo;

    @Column(name = "contact_address", columnDefinition = "varchar(255)")
    private String contactAddress;

    @Column(name = "gender", columnDefinition = "varchar(10)")
    private String gender;

    @Column(name = "open_date", columnDefinition = "date")
    private LocalDate openDate;

    @Column(name = "open_account_channel", columnDefinition = "enum('PINEX','ALPHA','OTHER')")
    @Enumerated(EnumType.STRING)
    private Constant.OpenAccountChannel openAccountChannel;

    @Override
    public String getAuthorities() {
        return Secure.ROLE_CUSTOMER;
    }

    @Override
    public Long getUserId() {
        return getId();
    }

    @JsonIgnore
    @Override
    public boolean isAdmin() {
        return false;
    }

    @JsonIgnore
    public String getJson() {
        try {
            return JSON.toPrettyJson(Collections.singletonList(this));
        } catch (Exception ex) {
            logger.error("Error during convert object customer to json \n" + ex);
        }
        return "[]";
    }
}
