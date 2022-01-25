package org.example.service;

import org.springframework.data.domain.Page;
import org.common.dto.response.ResponseDTOCursor;
import org.common.exception.BusinessException;
import org.example.dto.response.CustomerInfo;
import org.example.dto.response.CustomerThemeDto;
import org.example.dto.response.ThemeDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CustomerThemeService {
    Page<CustomerInfo> getWatchingCustomers(@NotBlank String themeCode, @NotNull Integer page, @NotNull Integer pageSize) throws BusinessException;

    List<ThemeDto> subscribeTheme(String themeCodes) throws BusinessException;

    List<ThemeDto> unsubscribeTheme(String themeCodes) throws BusinessException;

    List<ThemeDto> getAllActiveThemes() throws BusinessException;

    ThemeDto getThemeDetails(String code) throws BusinessException;

    List<ThemeDto> getSubscribedTheme() throws BusinessException;

    ResponseDTOCursor<List<CustomerThemeDto>> getCustomerSubscribeTheme(String themeCode, String cursor, Integer limit) throws BusinessException;
}
