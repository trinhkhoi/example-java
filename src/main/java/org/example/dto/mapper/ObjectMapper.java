package org.example.dto.mapper;

import org.springframework.stereotype.Component;
import org.common.dto.response.market.StockSuggestionResponse;
import org.example.dto.StockCodeSuggestionResponse;

@Component
public class ObjectMapper {
    public StockCodeSuggestionResponse convertFromStockSuggestionResponse(StockSuggestionResponse stockSuggestionResponse) {
        return StockCodeSuggestionResponse.builder()
                .stockCode(stockSuggestionResponse.getStockCode())
                .stockExchange(stockSuggestionResponse.getStockExchange())
                .name(stockSuggestionResponse.getName())
                .nameEn(stockSuggestionResponse.getNameEn())
                .shortName(stockSuggestionResponse.getShortName())
                .isHnx30(stockSuggestionResponse.getIsHnx30())
                .isVn30(stockSuggestionResponse.getIsVn30())
                .build();
    }
}
