package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.jpa.NativeQueryColumn;
import org.common.jpa.NativeQueryEntity;
import org.common.utils.DateUtil;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NativeQueryEntity
public class CustomerDto {
    @NativeQueryColumn(index = 0)
    private Long id;

    @JsonIgnore
    @NativeQueryColumn(index = 1)
    private String username;

    @JsonIgnore
    @NativeQueryColumn(index = 2)
    private String accountNo;

    @JsonIgnore
    @NativeQueryColumn(index = 3)
    private String subAccount;

    @JsonIgnore
    @NativeQueryColumn(index = 4)
    private String vsd;

    @JsonIgnore
    @NativeQueryColumn(index = 5)
    private String cif;

    @JsonIgnore
    @NativeQueryColumn(index = 6)
    private String address;

    @NativeQueryColumn(index = 7)
    private String position;

    @NativeQueryColumn(index = 8)
    private String name;

    @NativeQueryColumn(index = 9)
    private String avatar;

    @NativeQueryColumn(index = 10)
    private String caption;

    @NativeQueryColumn(index = 11)
    private Boolean isKol;

//    @NativeQueryColumn(index = 12)
//    private Boolean hasProAccount;

    @NativeQueryColumn(index = 12)
    private Boolean hasSyncContact;

    @JsonIgnore
    @NativeQueryColumn(index = 13)
    private String authDef;

    @JsonIgnore
    @NativeQueryColumn(index = 14)
    private Boolean firstLogin;

    @JsonIgnore
    @NativeQueryColumn(index = 15)
    private String phone;

    @JsonIgnore
    @NativeQueryColumn(index = 16)
    private String email;

    @NativeQueryColumn(index = 17)
    private String state;

    @JsonIgnore
    @NativeQueryColumn(index = 18)
    private String guid;

    @JsonIgnore
    @NativeQueryColumn(index = 19)
    private String deviceType;

    @NativeQueryColumn(index = 20)
    private String fcmToken;

    @NativeQueryColumn(index = 21)
    private Double kolPoint;

    @JsonIgnore
    @NativeQueryColumn(index = 22)
    private Date dob;

    @JsonIgnore
    public LocalDate getDob() {
        return dob != null ? new java.sql.Date(dob.getTime()).toLocalDate() : null;
    }

    @JsonIgnore
    @NativeQueryColumn(index = 23)
    private String identityCardNo;

    @JsonIgnore
    @NativeQueryColumn(index = 24)
    private String contactAddress;

    @JsonIgnore
    @NativeQueryColumn(index = 25)
    private String gender;

    @NativeQueryColumn(index = 26)
    private String displayName;

    @JsonIgnore
    @NativeQueryColumn(index = 27)
    private Timestamp deletedAt;

    @NativeQueryColumn(index = 28)
    private Timestamp createdAt;

    @NativeQueryColumn(index = 29)
    private Timestamp updatedAt;

    @JsonIgnore
    @NativeQueryColumn(index = 30)
    private Date openDate;

//    @JsonIgnore
//    @NativeQueryColumn(index = 36)
//    private Short custStat;

    public LocalDate getOpen() {
        return openDate != null ? new java.sql.Date(openDate.getTime()).toLocalDate() : null;
    }

    private Boolean isFollowed = true;

    private Integer totalFollowing;

    private Integer totalFollower;

    private List<CustomerLatestDto> latestFollowers;

    public String getCreatedAtStr() {
        return DateUtil.localDateTimeToStringT24(createdAt.toLocalDateTime());
    }

    private Boolean isFeatureProfile;
    private String coverImage;
    private String fullDes;

    public Double getKolPoint() {
        return kolPoint != null ? kolPoint : 0;
    }

    public Boolean getIsKol() {
        return isKol != null && isKol;
    }
}
