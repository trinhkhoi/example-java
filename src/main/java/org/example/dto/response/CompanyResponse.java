package org.example.dto.response;

import lombok.Data;
import org.example.util.Constant;

@Data
public class CompanyResponse {
    private String stockCode;
    private Constant.StockExchange stockExchange;
    private Boolean isVn30;
    private Boolean isHnx30;
    private String name;
    private String shortName;
    private String nameEn;
    private Double lastPrice;
    private Double change;
    private Double changePercent;
    private String id;
    private Double ceilPrice;
    private Double refPrice;
    private Double floorPrice;
    private Long lastVolume;
    private Double highPrice;
    private Double lowPrice;
}
