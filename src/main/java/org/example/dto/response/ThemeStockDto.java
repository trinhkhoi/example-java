package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeStockDto {
    private String stock_code;
    private String stock_name;
    private Double last_price;
    private Double change_price;
    private Double change_price_percent;
    private Double ref_price;
    private Double ceil_price;
    private Double floor_price;
}
