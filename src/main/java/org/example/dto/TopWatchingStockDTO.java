package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TopWatchingStockDTO {
    private String stockCode;
    private int totalCount;

    private String image;
    private String stockExchange;
    private String companyName;

    public TopWatchingStockDTO(String stockCode, int totalCount) {
        this.stockCode = stockCode;
        this.totalCount = totalCount;
    }

}
