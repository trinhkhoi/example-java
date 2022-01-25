package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerThemeDto {
    private Long id;
    private Long idCustomerTheme;
    private String fullName;
    private String avatar;
    private Boolean isKol;
    private Boolean isFeatureProfile;
    private Boolean isSharePortfolio;
    private Boolean isShareWatchlist;
    private Boolean isFollowed = false;
    private Integer totalFollowing = 0;
    private Integer totalFollower = 0;
}
