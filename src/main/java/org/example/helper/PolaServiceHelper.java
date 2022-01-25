package org.example.helper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.wts.StockInfo;
import org.example.dto.response.wts.SymbolData;
import org.example.dto.response.wts.SymbolDataList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.common.dto.request.wts.WTSRequest;
import org.common.dto.response.wts.ErrorMessageLanguage;
import org.common.dto.response.wts.WTSResponse;
import org.common.exception.BusinessException;
import org.example.dto.request.wts.GetThemeRequest;
import org.example.dto.response.wts.CoreTheme;
import org.example.util.AppProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.util.Constant.CHANNEL;
import static org.example.util.Constant.GROUP_CORE;

@Service
@Slf4j
public class PolaServiceHelper {
    private static Logger logger = LoggerFactory.getLogger(PolaServiceHelper.class);

    @Autowired
    @Qualifier("wtsRestTemplate")
    private RestTemplate restTemplate;
    @Autowired
    private AppProperties appProperties;

    /**
     * Method to get all themes from core system
     *
     * @param request
     * @return {@link CoreTheme}
     * @throws BusinessException
     */
    public CoreTheme getThemes(GetThemeRequest request) throws BusinessException {
        WTSRequest<GetThemeRequest> wtsRequest = WTSRequest.<GetThemeRequest>builder()
                .cmd("getThemeFullinfo")
                .data(request)
                .group(GROUP_CORE)
                .channel(CHANNEL)
                .build();
        WTSResponse<?> response = restTemplate.postForObject(appProperties.getWtsBaseUrl() + "/ThemeInfo", wtsRequest, WTSResponse.class);
        return (CoreTheme) parseResponseData(response, CoreTheme.class);
    }

    /**
     * Get stock data by list
     *
     * @param listStockCodes
     * @return
     * @throws BusinessException
     */
    public List<SymbolData> getStockData(String listStockCodes) {
        try {
            ResponseEntity response = restTemplate.getForEntity(appProperties.getWtsBaseUrl() + "/getliststockdata/" + listStockCodes, List.class);
            log.debug("call WTS getStockData response: " + response);
            return (SymbolDataList) parseSuccessData(response.getBody(), SymbolDataList.class);
        } catch (Exception ex) {
            logger.error("Error during get list stock data from core: " + listStockCodes);
        }
        return new ArrayList<>();
    }

    /**
     * Method to get all wts error messages
     *
     * @throws BusinessException
     */
    public void getListMessage() throws BusinessException {
        Map response = restTemplate.getForObject(appProperties.getWtsBaseUrl() + "/listMessage", Map.class);
        logger.info(response.toString());
        try {
            convertToMapWtsMessage(response);
        } catch (Exception ex) {
            logger.error("Error during parse list message to json");
        }

    }

    public StockInfo getStockInfo(String stockCode) throws BusinessException {
        try {
            StockInfo response = restTemplate.getForObject(appProperties.getWtsBaseUrl() + "/stockInfo.pt?symbol=" + stockCode, StockInfo.class);
            if (null != response) {
                log.debug("WTS stockInfo return: {}", response);
                return response;
            }
        } catch (Exception e) {
            throw new BusinessException("No response from get stockInfo wts, " + e.getMessage());
        }
        return null;
    }

    private void convertToMapWtsMessage(Map wtsMessageMap) {
        clearWtsErrorMessageMap();
        wtsMessageMap.keySet().forEach(code -> {
            Map message = (Map) wtsMessageMap.get(code);
            ErrorMessageLanguage messageLanguage = ErrorMessageLanguage.builder()
                    .vi(message.get("vi") == null ? null : message.get("vi").toString())
                    .en(message.get("en") == null ? null : message.get("en").toString())
                    .kr(message.get("kr") == null ? null : message.get("kr").toString())
                    .build();
            addWtsErrorMessage((String) code, messageLanguage);
        });
    }
}
