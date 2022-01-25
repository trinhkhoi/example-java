package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPortfolioBalanceDTO {
    private String stockCode;
    private long amount;
    private long preAmount;
    private String tradingDate;
}
