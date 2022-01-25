package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeDto {
    private String code;
    private String name;
    private String url;
    private String bgImage;
    private String type;
    private String description;
    private Integer totalSubscribe;
    private Boolean isSubsribed;
    private List<CustomerLatestDto> latestSubscribe;
    private List<ThemeStockDto> stockList;

    public int getTotalSubscribe() {
        return totalSubscribe == null ? 0 : totalSubscribe;
    }
}
