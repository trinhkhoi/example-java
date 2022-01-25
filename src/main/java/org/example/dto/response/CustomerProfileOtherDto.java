package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.common.jpa.NativeQueryEntity;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NativeQueryEntity
public class CustomerProfileOtherDto {
    private Long id;
    private String name;
    private String displayName;
    private String avatar;
    private String coverImage;
    private String caption;
    private Boolean isKol;
    private Boolean isFeatureProfile;
    private String fullDes;
    private Boolean hasProAccount;
    private Boolean hasSyncContact;
    private Boolean isSharePortfolio;
    private Boolean isShareWatchlist;
    private String state;
    private Timestamp createdAt;
    private Boolean isFollowed;
    private Integer totalFollowing;
    private Integer totalFollower;
    private String position;
}
