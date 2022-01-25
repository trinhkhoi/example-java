package org.example.service;

import org.example.dto.response.ThemeDto;
import org.example.entity.Customer;
import org.example.entity.Theme;
import org.common.dto.response.pist.ThemeDetailsData;
import org.common.exception.BusinessException;

import java.util.List;
import java.util.Map;

public interface ThemeService {

    List<Theme> saveAndIncreaseSubscribe(Customer customer, List<String> themeCodes) throws BusinessException;

    Theme decreaseSubscribe(String themeCode, List<Customer> customerSubscribes) throws BusinessException;

    Map<String, Theme> getMapThemes() throws BusinessException;

    void importThemeFromCore() throws BusinessException;

    List<ThemeDto> getThemesByStockCode(String stockCode) throws BusinessException;

    List<ThemeDto> getThemeDetails(List<String> themeCodes, boolean loadDetail) throws BusinessException;

    /**
     * used to sync-up data with community-service
     */
    List<ThemeDetailsData> getAllThemes() throws BusinessException;
}
