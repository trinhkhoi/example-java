package org.example.dto.response.wts;

import lombok.Data;

@Data
public class ThemeSymbol {
    private int reqCount;
    private String symbol;
    private String symbolNm;
    private String curPrice;
    private int ord;
}
