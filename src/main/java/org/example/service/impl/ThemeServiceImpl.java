package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.request.wts.GetThemeRequest;
import org.example.dto.response.CustomerLatestDto;
import org.example.dto.response.wts.CoreTheme;
import org.example.dto.response.wts.OriginTheme;
import org.example.dto.response.wts.SymbolData;
import org.example.dto.response.wts.ThemeSymbol;
import org.example.entity.Theme;
import org.example.entity.ThemeDetail;
import org.example.helper.PolaServiceHelper;
import org.example.repository.CustomerThemeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.common.dto.response.pist.ThemeDetailsData;
import org.common.exception.BusinessException;
import org.common.utils.DataUtil;
import org.common.utils.JSON;
import org.example.dto.response.ThemeDto;
import org.example.dto.response.ThemeStockDto;
import org.example.entity.Customer;
import org.example.repository.ThemeDetailRepository;
import org.example.repository.ThemeRepository;
import org.example.service.ThemeService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.example.util.Constant.SPLIT_COMMA;

@Slf4j
@Service
public class ThemeServiceImpl extends BaseServiceImpl implements ThemeService {
    private static Logger logger = LoggerFactory.getLogger(ThemeServiceImpl.class);

    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ThemeDetailRepository themeDetailRepository;
    @Autowired
    private CustomerThemeRepository customerThemeRepository;
    @Autowired
    private PolaServiceHelper polaServiceHelper;

    @Override
    public List<Theme> saveAndIncreaseSubscribe(Customer customer, List<String> themeCodes) throws BusinessException {
        // update existed themes
        List<Theme> existedThemes = themeRepository.findByListThemeCode(themeCodes);
        List<String> existedThemeCodes = existedThemes.stream().map(Theme::getThemeCode).collect(Collectors.toList());
        for (Theme theme : existedThemes) {
            String latestSubscribe = buildJsonCustomerSubscribe(customer, theme);
            theme.setLastestSubscribe(StringUtils.isBlank(latestSubscribe) ? theme.getLastestSubscribe() : latestSubscribe);
            theme.setTotalSubsribe(theme.getTotalSubsribe() + 1);
        }
        // add new records if not exists
        List<Theme> newThemes = themeCodes.stream()
                .filter(themeCode -> !existedThemeCodes.contains(themeCode))
                .map(themeCode -> {
                    List<CustomerLatestDto> listCustomerSubscribeThemes = singletonList(CustomerLatestDto
                            .builder()
                            .avatar(customer.getAvatar())
                            .idCustomer(customer.getId())
                            .build());

                    return Theme.builder()
                            .themeCode(themeCode)
                            .totalSubsribe(1)
                            .lastestSubscribe(convertListToJson(listCustomerSubscribeThemes))
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                }).collect(Collectors.toList());
        return themeRepository.saveAll(Stream.concat(existedThemes.stream(), newThemes.stream()).collect(Collectors.toList()));
    }

    @Override
    public Theme decreaseSubscribe(String themeCode, List<Customer> customerSubscribes) throws BusinessException {
        Theme existedTheme = themeRepository.findByThemeCode(themeCode);
        if (existedTheme != null) {
            String latestSubscribe = buildJsonCustomerSubscribeFromList(customerSubscribes);
            existedTheme.setLastestSubscribe(StringUtils.isBlank(latestSubscribe) ? existedTheme.getLastestSubscribe() : latestSubscribe);
            existedTheme.setTotalSubsribe(existedTheme.getTotalSubsribe() - 1);
            return themeRepository.save(existedTheme);
        } else {
            log.error("The theme was not found. themeCode: {}", themeCode);
            throw new BusinessException("The theme was not found");
        }
    }

    @Override
    public Map<String, Theme> getMapThemes() {
        try {
            List<Theme> themes = themeRepository.findAll();
            Map<String, Theme> mapThemes;
            mapThemes = themes.stream().collect(Collectors.toMap(Theme::getThemeCode, Function.identity()));
            return mapThemes;
        } catch (Exception ex) {
            logger.error("error during get map of theme subscribe");
        }
        return new HashMap<>();
    }

    @Override
    public List<ThemeDto> getThemesByStockCode(String stockCode) throws BusinessException {
        if (StringUtils.isBlank(stockCode)) {
            return new ArrayList<>();
        }

        List<Theme> themes = themeRepository.findAllThemeByStockCode(singletonList(stockCode));
        List<String> themeCodes = themes.stream().map(Theme::getThemeCode).collect(Collectors.toList());
        List<String> subscribedThemes = customerThemeRepository.findListCustomerThemes(getAuthentication().getUserId());
        Map<String, List<ThemeStockDto>> mapThemeDetails = getMapThemeDetail(themeCodes);

        return themes.stream()
                .map(theme -> ThemeDto.builder()
                        .code(theme.getThemeCode())
                        .name(theme.getThemeName())
                        .description(theme.getDescription())
                        .latestSubscribe(theme.getListLastestSubscribe())
                        .stockList(mapThemeDetails.get(theme.getThemeCode()))
                        .totalSubscribe(theme.getTotalSubsribe())
                        .type(theme.getThemeType())
                        .isSubsribed(subscribedThemes.contains(theme.getThemeCode()))
                        .url(theme.getUrl())
                        .bgImage(theme.getBgImage())
                        .build()
                )
                .sorted(Comparator.comparingInt(ThemeDto::getTotalSubscribe).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ThemeDto> getThemeDetails(List<String> themeCodes, boolean loadDetail) throws BusinessException {
        if (themeCodes == null || themeCodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Theme> themes = themeRepository.findByListThemeCode(themeCodes);
        themeCodes = themes.stream().map(Theme::getThemeCode).collect(Collectors.toList());
        Map<String, List<ThemeStockDto>> mapThemeDetails;
        if (loadDetail) {
            mapThemeDetails = getMapThemeDetail(themeCodes);
            return themes.stream().map(theme -> ThemeDto.builder()
                    .code(theme.getThemeCode())
                    .name(theme.getThemeName())
                    .description(theme.getDescription())
                    .latestSubscribe(theme.getListLastestSubscribe())
                    .stockList(mapThemeDetails.get(theme.getThemeCode()))
                    .totalSubscribe(theme.getTotalSubsribe())
                    .type(theme.getThemeType())
                    .url(theme.getUrl())
                    .bgImage(theme.getBgImage())
                    .build()).collect(Collectors.toList());
        } else {
            return themes.stream().map(theme -> ThemeDto.builder()
                    .code(theme.getThemeCode())
                    .name(theme.getThemeName())
                    .description(theme.getDescription())
                    .latestSubscribe(theme.getListLastestSubscribe())
                    .stockList(new ArrayList<>())
                    .totalSubscribe(theme.getTotalSubsribe())
                    .type(theme.getThemeType())
                    .url(theme.getUrl())
                    .bgImage(theme.getBgImage())
                    .build()).collect(Collectors.toList());
        }
    }

    @Override
    public List<ThemeDetailsData> getAllThemes() throws BusinessException {
        List<Theme> themeList = themeRepository.findAll();
        List<ThemeDetail> themeDetailList = themeDetailRepository.findAll();
        Map<String, List<String>> themeDetailMap = themeDetailList.stream().collect(Collectors
                .groupingBy(ThemeDetail::getThemeCode, Collectors.mapping(ThemeDetail::getStockCode, Collectors.toList())));
        return themeList.stream()
                .map(theme -> ThemeDetailsData.builder()
                        .bgImage(theme.getBgImage())
                        .code(theme.getThemeCode())
                        .name(theme.getThemeName())
                        .description(theme.getDescription())
                        .type(theme.getThemeType())
                        .url(theme.getUrl())
                        .stocks(themeDetailMap.get(theme.getThemeCode()))
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public void importThemeFromCore() throws BusinessException {

        // Get all theme from core
        List<OriginTheme> originThemes = getAllThemesFromCore();

        Map<String, Theme> mapThemes = getMapThemes();
        originThemes.forEach(originTheme -> {
            if (mapThemes.get(originTheme.getThsCode()) != null) {
                // handle existed theme
                updateExistedTheme(mapThemes, originTheme);
            } else {
                // handle new theme
                addThemeAndDetailTheme(originTheme);
            }
        });

        // deleted theme which was not received from core
        Map<String, OriginTheme> mapOriginTheme = originThemes.stream().collect(Collectors.toMap(OriginTheme::getThsCode, Function.identity()));
        mapThemes.values().forEach(theme -> {
            if (mapOriginTheme.get(theme.getThemeCode()) == null) {
                themeRepository.deleteTheme(theme.getThemeCode());
                themeDetailRepository.deleteThemeDetailByThemeCode(theme.getThemeCode());
                customerThemeRepository.deleteByThemeCode(theme.getThemeCode());
            }
        });
    }

    private Map<String, List<ThemeStockDto>> getMapThemeDetail(List<String> themeCodes) {
        List<ThemeDetail> themeDetails = themeDetailRepository.findAllByListThemeCode(themeCodes);
        Set<String> stockCodes = themeDetails.stream().map(ThemeDetail::getStockCode).collect(Collectors.toSet());
        Map<String, SymbolData> stocksData = getStockDataFromCore(stockCodes);
        Map<String, List<ThemeStockDto>> mapThemeDetails = new HashMap<>();
        themeDetails.forEach(themeDetail -> {
            SymbolData stockData = stocksData.get(themeDetail.getStockCode());
            ThemeStockDto stockDto = ThemeStockDto.builder()
                    .stock_code(themeDetail.getStockCode())
                    .stock_name(themeDetail.getStockName())
                    .last_price(DataUtil.safeToDouble(stockData.getLastPrice()))
                    .change_price(DataUtil.safeToDouble(stockData.getChangeValue()))
                    .change_price_percent(DataUtil.safeToDouble(stockData.getChangePc()))
                    .ref_price(DataUtil.safeToDouble(stockData.getRefPrice()))
                    .ceil_price(DataUtil.safeToDouble(stockData.getCeilPrice()))
                    .floor_price(DataUtil.safeToDouble(stockData.getFloorPrice()))
                    .build();
            if (mapThemeDetails.get(themeDetail.getThemeCode()) == null) {
                mapThemeDetails.put(themeDetail.getThemeCode(), new ArrayList<>(singletonList(stockDto)));
            } else {
                List<ThemeStockDto> existedStockDtos = mapThemeDetails.get(themeDetail.getThemeCode());
                existedStockDtos.add(stockDto);
                mapThemeDetails.put(themeDetail.getThemeCode(), existedStockDtos);
            }
        });
        return mapThemeDetails;
    }

    private Map<String, SymbolData> getStockDataFromCore(Set<String> stockCodes) {
        String listStockCodes = String.join(SPLIT_COMMA, stockCodes);
        List<SymbolData> stockData = polaServiceHelper.getStockData(listStockCodes);
        Map<String, SymbolData> mapStockCore = stockData.stream()
                .collect(Collectors.toMap(SymbolData::getStockCode, Function.identity()));
        return mapStockCore;
    }

    private List<OriginTheme> getAllThemesFromCore() throws BusinessException {
        List<OriginTheme> originThemes = new ArrayList<>();
        String nexKey = "";
        boolean isContinue = true;
        while (isContinue) {
            GetThemeRequest themeRequest = GetThemeRequest.builder().nextKey(nexKey).reqCount(100).build();
            CoreTheme coreTheme = polaServiceHelper.getThemes(themeRequest);
            originThemes.addAll(coreTheme.getList());
            isContinue = coreTheme.getContinued();
            nexKey = coreTheme.getNextKey();
        }
        return originThemes;
    }

    private void updateExistedTheme(Map<String, Theme> mapThemes, OriginTheme originTheme) {
        Theme existedTheme = mapThemes.get(originTheme.getThsCode());
        existedTheme.setThemeName(originTheme.getThsName());
        existedTheme.setThemeType(originTheme.getThsType());
        existedTheme.setDescription(originTheme.getRmrk());
        existedTheme.setWrktmp(originTheme.getWrktmp());
        existedTheme.setUrl(originTheme.getThsUrlPic());
        themeRepository.save(existedTheme);

        List<ThemeDetail> themeDetails = themeDetailRepository.findAllByThemeCode(originTheme.getThsCode());
        Map<String, ThemeSymbol> mapThemeSymbol = originTheme.getThemdtl().stream().collect(Collectors.toMap(ThemeSymbol::getSymbol, Function.identity()));

        // Remove stock in theme
        themeDetails.forEach(themeDetail -> {
            if (mapThemeSymbol.get(themeDetail.getStockCode()) == null) {
                themeDetailRepository.deleteThemeDetailByStockCode(themeDetail.getStockCode());
            }
        });

        // Add and update stock info of theme
        Map<String, ThemeDetail> mapThemeDetail = themeDetails.stream().collect(Collectors.toMap(ThemeDetail::getStockCode, Function.identity()));
        originTheme.getThemdtl().forEach(themeSymbol -> {
            if (mapThemeDetail.get(themeSymbol.getSymbol()) == null) {
                themeDetailRepository.save(ThemeDetail.builder()
                        .stockCode(themeSymbol.getSymbol())
                        .stockName(themeSymbol.getSymbolNm())
                        .currentPrice(themeSymbol.getCurPrice())
                        .themeCode(originTheme.getThsCode())
                        .ord(themeSymbol.getOrd())
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .build());
            } else {
                ThemeDetail themeDetail = mapThemeDetail.get(themeSymbol.getSymbol());
                themeDetail.setCurrentPrice(themeSymbol.getCurPrice());
                themeDetail.setOrd(themeSymbol.getOrd());
                themeDetail.setStockName(themeSymbol.getSymbolNm());
                themeDetail.setUpdatedAt(LocalDateTime.now());
                themeDetailRepository.save(themeDetail);
            }
        });
    }

    private void addThemeAndDetailTheme(OriginTheme originTheme) {
        if (!DataUtil.isNullOrEmpty(originTheme.getThsCode()) && !DataUtil.isNullOrEmpty(originTheme.getThsName())) {
            themeRepository.save(Theme.builder()
                    .lastestSubscribe(StringUtils.EMPTY)
                    .themeCode(originTheme.getThsCode())
                    .totalSubsribe(0)
                    .themeName(originTheme.getThsName())
                    .themeType(originTheme.getThsType())
                    .description(originTheme.getRmrk())
                    .wrktmp(originTheme.getWrktmp())
                    .url(originTheme.getThsUrlPic())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
            List<ThemeDetail> detailThemes = originTheme.getThemdtl().stream().map(stock -> ThemeDetail.builder()
                    .stockCode(stock.getSymbol())
                    .stockName(stock.getSymbolNm())
                    .currentPrice(stock.getCurPrice())
                    .themeCode(originTheme.getThsCode())
                    .ord(stock.getOrd())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build()).collect(Collectors.toList());
            themeDetailRepository.saveAll(detailThemes);
        }
    }

    private String buildJsonCustomerSubscribe(Customer customer, Theme existedTheme) {
        try {
            List latestSubscribes = existedTheme.getListLastestSubscribe();
            if (latestSubscribes.size() >= 3) {
                latestSubscribes.remove(2);
            }
            latestSubscribes.add(CustomerLatestDto
                    .builder()
                    .idCustomer(customer.getId())
                    .avatar(customer.getAvatar())
                    .build());
            return convertListToJson(latestSubscribes);
        } catch (Exception ex) {
            logger.error("Error during build latest customer subscribe");
        }
        return StringUtils.EMPTY;
    }

    private String buildJsonCustomerSubscribeFromList(List<Customer> customers) {
        try {
            List<CustomerLatestDto> listCustomerSubscribes = customers
                    .stream()
                    .map(customer -> CustomerLatestDto
                            .builder()
                            .idCustomer(customer.getId())
                            .avatar(customer.getAvatar())
                            .build())
                    .collect(Collectors.toList());
            return convertListToJson(listCustomerSubscribes);
        } catch (Exception ex) {
            logger.error("Error during build latest customer subscribe from list");
        }
        return StringUtils.EMPTY;
    }

    public String convertListToJson(List<CustomerLatestDto> listCustomerSubscribeThemes) {
        try {
            return JSON.toPrettyJson(listCustomerSubscribeThemes);
        } catch (Exception ex) {
            logger.error("Error during convert object customer to json \n" + ex);
        }
        return "";
    }
}
