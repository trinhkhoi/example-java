package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockCodeSuggestionResponse {
    protected String stockCode;
    protected String stockExchange;
    protected Boolean isVn30;
    protected Boolean isHnx30;
    protected String name;
    protected String shortName;
    protected String nameEn;
    private int score;
    private Double ceilPrice;
    private Double refPrice;
    private Double floorPrice;
    private Double lastPrice;
    private Long lastVolume;
    private Double change;
    private Double perChange;
    private Double highPrice;
    private Double lowPrice;
}
