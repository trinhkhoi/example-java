package org.example.helper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CompanyListResponse;
import org.example.dto.response.CompanyResponse;
import org.example.dto.response.CompanyShortInfoListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.common.dto.response.market.StockSuggestionResponse;
import org.common.exception.BusinessException;
import org.pist.dto.response.*;
import org.example.util.AppProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MarketServiceHelper {

    private static Logger logger = LoggerFactory.getLogger(MarketServiceHelper.class);

    @Autowired
    @Qualifier("restTemplateSystem")
    private RestTemplate restTemplateSystem;

    @Autowired
    private AppProperties appProperties;

    /**
     * used for receive stocks of a watchlist
     *
     * @param stockCodes
     * @return
     * @throws BusinessException
     */
    public List<CompanyResponse> getCompaniesByCodes(Collection<String> stockCodes) {
        if (stockCodes == null || stockCodes.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            String url = appProperties.getMarketBaseUrl() + "/system/company/get-by-stocks?stockCodes=" + String.join(",", stockCodes);
            CompanyListResponse response = restTemplateSystem.getForObject(url, CompanyListResponse.class);
            return response != null ? response.getData() : new ArrayList<>();
        } catch (Exception ex) {
            logger.error("Error during get stock info from market: " + stockCodes);
        }
        return new ArrayList<>();
    }

    public List<StockSuggestionResponse> getStockCodeInSameCompanyGroup(List<String> stockCodes, int limit) {
        try {
            String url = appProperties.getMarketBaseUrl() + "/system/company/ver2/get-by-company-group?limit=" + limit;
            CompanyShortInfoListResponse response = restTemplateSystem.postForObject(url, stockCodes, CompanyShortInfoListResponse.class);
            log.debug("call market-service: " + response);
            assert response != null;
            return response.getData();
        } catch (Exception e) {
            logger.error("Error during get stock info from market");
        }
        return new ArrayList<>();
    }
}
