package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.response.PageData;
import org.example.entity.CustomerTheme;
import org.example.service.CustomerThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.common.dto.kafka.ThemeMessage;
import org.common.dto.response.ResponseDTOCursor;
import org.common.exception.BusinessException;
import org.common.helper.kafka.PineKafkaProperties;
import org.common.utils.DataUtil;
import org.example.dto.response.CustomerInfo;
import org.example.dto.response.CustomerThemeDto;
import org.example.dto.response.ThemeDto;
import org.example.entity.Customer;
import org.example.entity.Theme;
import org.example.repository.CustomerThemeRepository;
import org.example.service.CustomerFriendService;
import org.example.service.CustomerService;
import org.example.service.ThemeService;
import org.example.util.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class CustomerThemeServiceImpl extends BaseServiceImpl implements CustomerThemeService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private CustomerThemeRepository customerThemeRepository;
    @Autowired
    private KafkaTemplate<String, ThemeMessage> themeKafkaTemplate;
    @Autowired
    private CustomerFriendService customerFriendService;

    @Override
    public Page<CustomerInfo> getWatchingCustomers(@NotBlank String themeCode, @NotNull Integer page, @NotNull Integer pageSize) throws BusinessException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CustomerTheme> customerThemePage = customerThemeRepository.findAllByThemeCodeOrderByCreatedAtDesc(themeCode, pageable);
        List<String> customerIds = customerThemePage.stream().map(customerTheme -> String.valueOf(customerTheme.getIdCustomer())).collect(Collectors.toList());
        List<CustomerInfo> customerInfoList = customerService.getCustomerInfoByIds(customerIds);
        return new PageData<>(customerInfoList, pageable, customerThemePage.getTotalElements());
    }

    @Override
    public List<ThemeDto> subscribeTheme(String themeCodes) throws BusinessException {
        Customer customer = customerService.getCurrentCustomer();
        if (!DataUtil.isNullOrEmpty(themeCodes)) {
            List<String> themeCodeList = Arrays.asList(themeCodes.split(","));
            List<String> existedCustomerThemes = customerThemeRepository.findListIdByCustomerAndTheme(customer.getId(), themeCodeList);
            List<CustomerTheme> newCustomerThemes = themeCodeList.stream()
                    .filter(themeCode -> !existedCustomerThemes.contains(themeCode))
                    .map(themeCode -> CustomerTheme.builder()
                            .idCustomer(customer.getUserId())
                            .themeCode(themeCode)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());
            newCustomerThemes = customerThemeRepository.saveAll(newCustomerThemes);
            // increase number of subscribe for themeCode
            List<Theme> newThemes = themeService.saveAndIncreaseSubscribe(customer, newCustomerThemes.stream().map(CustomerTheme::getThemeCode).collect(Collectors.toList()));
            newThemes.forEach(theme -> {
                try {
                    themeKafkaTemplate.send(PineKafkaProperties.TOPIC_THEME, ThemeMessage.builder()
                            .customerId(customer.getId())
                            .list(Collections.singletonList(ThemeMessage.ItemInfo.builder()
                                    .themeCode(theme.getThemeCode())
                                    .themeName(theme.getThemeName())
                                    .subscribe(true)
                                    .build()))
                            .timestamp(System.currentTimeMillis())
                            .build());
                } catch (Exception ex) {
                    log.error("unable to send Kafka message. customerId: {}; topic: {}; themeCode: {}", customer.getId(), PineKafkaProperties.TOPIC_THEME, theme.getThemeCode());
                    log.error(ex.getMessage());
                }
            });
            return newThemes.stream()
                    .map(theme -> ThemeDto.builder()
                            .code(theme.getThemeCode())
                            .name(theme.getThemeName())
                            .description(theme.getDescription())
                            .latestSubscribe(theme.getListLastestSubscribe())
                            .totalSubscribe(theme.getTotalSubsribe())
                            .type(theme.getThemeType())
                            .isSubsribed(true)
                            .url(theme.getUrl())
                            .bgImage(theme.getBgImage())
                            .build()
                    )
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<ThemeDto> unsubscribeTheme(String themeCodes) throws BusinessException {
        Long customerId = getAuthentication().getUserId();
        List<String> themeCodeList = Arrays.asList(themeCodes.split(","));
        List<String> existedThemeCodes = customerThemeRepository.findListIdByCustomerAndTheme(customerId, themeCodeList);
        customerThemeRepository.deleteByIdCustomerAndThemeCodes(customerId, existedThemeCodes);

        // decrease number of unsubscribe for themeCode
        List<Theme> themes = new ArrayList<>();
        for (String themeCode : existedThemeCodes) {
            // TODO: 9/16/21 need improvement: avoid queries in loop
            List<Customer> listLatestCustomerSubscribes = customerService.getLatestCustomerSubscribeTheme(customerId, themeCode);
            try {
                Theme theme = themeService.decreaseSubscribe(themeCode, listLatestCustomerSubscribes);
                themes.add(theme);
                try {
                    themeKafkaTemplate.send(PineKafkaProperties.TOPIC_THEME, ThemeMessage.builder()
                            .customerId(customerId)
                            .list(Collections.singletonList(ThemeMessage.ItemInfo.builder()
                                    .themeCode(themeCode)
                                    .themeName(theme.getThemeName())
                                    .subscribe(false)
                                    .build()))
                            .timestamp(System.currentTimeMillis())
                            .build());
                } catch (Exception ex) {
                    log.error("unable to send Kafka message. customerId: {}; topic: {}; themeCode: {}", customerId, PineKafkaProperties.TOPIC_THEME, themeCode);
                    log.error(ex.getMessage());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return themes.stream()
                .map(theme -> ThemeDto.builder()
                        .code(theme.getThemeCode())
                        .name(theme.getThemeName())
                        .description(theme.getDescription())
                        .latestSubscribe(theme.getListLastestSubscribe())
                        .totalSubscribe(theme.getTotalSubsribe())
                        .type(theme.getThemeType())
                        .isSubsribed(false)
                        .url(theme.getUrl())
                        .bgImage(theme.getBgImage())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<ThemeDto> getAllActiveThemes() throws BusinessException {
        Map<String, Theme> mapThemeSubscribes = themeService.getMapThemes();
        List<String> subscribedThemes = customerThemeRepository.findListCustomerThemes(getAuthentication().getUserId());
        List<ThemeDto> list = themeService.getThemeDetails(new ArrayList<>(mapThemeSubscribes.keySet()), Boolean.FALSE);
        list.forEach(themeDto -> themeDto.setIsSubsribed(subscribedThemes.contains(themeDto.getCode())));
        return list;
    }

    @Override
    public List<ThemeDto> getSubscribedTheme() throws BusinessException {
        Long customerId = getAuthentication().getUserId();
        List<String> customerThemeCodes = customerThemeRepository.findListCustomerThemes(customerId);
        List<ThemeDto> list = themeService.getThemeDetails(customerThemeCodes, Boolean.TRUE);
        list.forEach(themeDto -> themeDto.setIsSubsribed(true));
        return list;
    }

    @Override
    public ThemeDto getThemeDetails(String code) throws BusinessException {
        List<String> subscribedThemes = customerThemeRepository.findListCustomerThemes(getAuthentication().getUserId());
        List<ThemeDto> list = themeService.getThemeDetails(Collections.singletonList(code), Boolean.TRUE);
        if (list == null || list.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.Error.THEME_NOT_FOUND);
        }
        list.get(0).setIsSubsribed(subscribedThemes != null && subscribedThemes.contains(list.get(0).getCode()));
        return list.get(0);
    }

    @Override
    public ResponseDTOCursor<List<CustomerThemeDto>> getCustomerSubscribeTheme(String themeCode, String cursor, Integer limit) throws BusinessException {
        List<CustomerTheme> customerThemeList;
        if (StringUtils.isBlank(cursor)) {
            // first page
            customerThemeList = customerThemeRepository.findAllByThemeCodeFirstPage(themeCode, limit);
        } else {
            try {
                customerThemeList = customerThemeRepository.findAllByThemeCode(themeCode, Long.valueOf(cursor), limit);
            } catch (Exception e) {
                log.warn("cursor must be a number");
                log.warn(e.getMessage());
                throw new BusinessException("cursor must be a number");
            }
        }
        Map<Long, CustomerTheme> customerThemeMap = customerThemeList.stream().collect(Collectors.toMap(CustomerTheme::getIdCustomer, Function.identity()));
        List<Long> idCustomers = new ArrayList<>(customerThemeMap.keySet());
        List<Customer> customerList = customerService.findAllByIdIsIn(idCustomers);
        List<Long> followings = customerFriendService.getListFollowingByListFriends(getCurrentCustomerId(), idCustomers);

        List<CustomerThemeDto> list = customerList.stream()
                .map(customer -> {
                            Long customerId = customer.getId();
                            return CustomerThemeDto.builder()
                                    .id(customerId)
                                    .idCustomerTheme(customerThemeMap.get(customerId).getId())
                                    .avatar(customer.getAvatar())
                                    .fullName(customer.getName())
                                    .isFollowed(followings.contains(customerId))
                                    .build();
                        }
                )
                .collect(Collectors.toList());

        String newCursor = list.size() >= limit ? list.get(list.size() - 1).getIdCustomerTheme().toString() : "";

        return ResponseDTOCursor.<List<CustomerThemeDto>>builder()
                .data(list)
                .cursor(newCursor)
                .build();
    }
}
