package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.StockCodeSuggestionResponse;
import org.example.dto.TopWatchingStockDTO;
import org.example.dto.mapper.ObjectMapper;
import org.example.dto.request.CreateUpdateWatchlistRequest;
import org.example.dto.response.CompanyResponse;
import org.example.dto.response.WatchlistResponse;
import org.example.dto.response.wts.StockInfo;
import org.example.dto.response.wts.SymbolData;
import org.example.entity.Watchlist;
import org.example.entity.WatchlistStock;
import org.example.helper.MarketServiceHelper;
import org.example.helper.PolaServiceHelper;
import org.example.repository.WatchlistRepository;
import org.example.repository.WatchlistStockRepository;
import org.example.service.WatchlistService;
import org.example.util.Constant;
import org.example.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.common.dto.kafka.WatchlistMessage;
import org.common.dto.response.ResponseDTOCursor;
import org.common.dto.response.market.StockSuggestionResponse;
import org.common.exception.BusinessException;
import org.common.helper.kafka.PineKafkaProperties;
import org.common.utils.DataUtil;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.util.Constant.SPLIT_COMMA;

@Service
@Slf4j
public class WatchlistServiceImpl extends BaseServiceImpl implements WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;
    @Autowired
    private WatchlistStockRepository watchlistStockRepository;
    @Autowired
    private MarketServiceHelper marketServiceHelper;
    @Autowired
    private PolaServiceHelper polaServiceHelper;
    @Autowired
    private KafkaTemplate<String, WatchlistMessage> watchlistKafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Watchlist createWatchlist(CreateUpdateWatchlistRequest request) throws BusinessException {
        // user can create only one watchlist by now
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findFirstByCustomerId(getAuthentication().getUserId());
        Watchlist newWatchlist = null;
        if (!optionalWatchlist.isPresent()) {
            newWatchlist = watchlistRepository.save(Watchlist.builder()
                    .createdAt(LocalDateTime.now())
                    .customerId(getAuthentication().getUserId())
                    .updatedAt(LocalDateTime.now())
                    .name(request.getName())
                    .share(request.getShare())
                    .build());
        } else {
            newWatchlist = optionalWatchlist.get();
        }

        // save new list
        if (!StringUtils.isBlank(request.getStocks())) {
            saveCustomerWatchlistStocks(request.getStocks().replaceAll("\\s+", ""), newWatchlist.getId());
        }

        return newWatchlist;
    }


    private void validateRequest(CreateUpdateWatchlistRequest request) throws BusinessException {
        if (StringUtils.isBlank(request.getName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.Error.WATCHLIST_NAME_REQUIRED);
        }
    }

    @Override
    public Watchlist updateWatchlist(Long watchlistId, CreateUpdateWatchlistRequest request) throws BusinessException {
        validateRequest(request);
        Watchlist watchlist = findWatchlistById(watchlistId);
        validateOwner(watchlist);
        watchlist.setName(request.getName());
        watchlist.setShare(request.getShare());
        watchlist.setUpdatedAt(LocalDateTime.now());
        watchlist = watchlistRepository.save(watchlist);

        // save new list
        if (!StringUtils.isBlank(request.getStocks())) {
            saveCustomerWatchlistStocks(request.getStocks().replaceAll("\\s+", ""), watchlist.getId());
        }
        return watchlist;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void shareWatchlist(Long idCustomer, boolean isShare) throws BusinessException {
        List<Watchlist> watchlists = watchlistRepository.findAllByCustomerId(idCustomer);
        watchlists.forEach(watchList -> {
            watchList.setShare(isShare);
        });
        watchlistRepository.saveAll(watchlists);
    }

    @Override
    public void removeStockFromWatchlist(String stockCode) throws BusinessException {
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findFirstByCustomerId(getAuthentication().getUserId());
        if (!optionalWatchlist.isPresent()) {
            return;
        }
        // remove stock from watchlist
        WatchlistStock watchlistStock = watchlistStockRepository.findByWatchlistIdAndStockCode(optionalWatchlist.get().getId(), stockCode);
        watchlistStock.setDeletedAt(LocalDateTime.now());
        watchlistStockRepository.save(watchlistStock);

        watchlistKafkaTemplate.send(PineKafkaProperties.TOPIC_WATCHLIST, WatchlistMessage.builder()
                .customerId(getCurrentCustomerId())
                .list(Collections.singletonList(WatchlistMessage.ItemInfo.builder()
                        .stockCode(stockCode)
                        .watch(false)
                        .build()))
                .timestamp(System.currentTimeMillis())
                .build());
    }

    @Override
    public List<CompanyResponse> getWatchlistStocks(Long watchlistId) throws BusinessException {
        Watchlist watchlist = findWatchlistByIdAndIdCustomer(getAuthentication().getUserId(), watchlistId);
        validateOwner(watchlist);
        return marketServiceHelper.getCompaniesByCodes(watchlistStockRepository.findAllByWatchlistId(watchlistId)
                .stream()
                .map(WatchlistStock::getStockCode)
                .collect(Collectors.toList()));
    }

    @Override
    public List<WatchlistResponse> getWatchlistList() throws BusinessException {
        return getListWatchlistByIdCustomer(getAuthentication().getUserId(), true);
    }

    @Override
    public List<WatchlistResponse> getWatchlistListOtherCustomer(Long idCustomer) throws BusinessException {
        return getListWatchlistByIdCustomer(idCustomer, false);
    }

    private List<WatchlistResponse> getListWatchlistByIdCustomer(Long idCustomer, boolean isOwner) throws BusinessException {
        boolean isViewOther = !isOwner;
        List<Watchlist> watchlistList = watchlistRepository.findAllByCustomerId(idCustomer);
        List<WatchlistResponse> response = watchlistList.stream().map(watchlist -> {
            List<CompanyResponse> companies = getListStocks(watchlist.getId());
            return WatchlistResponse.builder()
                    .name(watchlist.getName())
                    .customerId(watchlist.getCustomerId())
                    .share(watchlist.getShare())
                    .stocks(companies)
                    .build();
        }).collect(Collectors.toList());
        if (response.isEmpty()) {
            response.add(WatchlistResponse.builder()
                    .stocks(new ArrayList<>())
                    .build()
            );
        }
        return response;
    }

    private List<CompanyResponse> getListStocks(Long idWatchList) {
        List<WatchlistStock> watchlistStocks = watchlistStockRepository.findAllByWatchlistId(idWatchList);
        Set<String> stockCodes = watchlistStocks.stream().map(WatchlistStock::getStockCode).collect(Collectors.toSet());
        List<CompanyResponse> companies = marketServiceHelper.getCompaniesByCodes(stockCodes); // TODO: 5/11/21 Warn: query in a loop. Because user can create only one watchlist so this is acceptable
        if (!stockCodes.isEmpty()) {
            Map<String, SymbolData> mapStockDataCore = getStockDataFromCore(stockCodes);
            companies.forEach(company -> {
                SymbolData stockData = mapStockDataCore.get(company.getStockCode());
                if (stockData != null) {
                    company.setChange(DataUtil.safeToDouble(stockData.getChangeValue()));
                    company.setChangePercent(DataUtil.safeToDouble(stockData.getChangePc()));
                    company.setLastPrice(DataUtil.safeToDouble(stockData.getLastPrice()));
                    company.setCeilPrice(DataUtil.safeToDouble(stockData.getCeilPrice()));
                    company.setFloorPrice(DataUtil.safeToDouble(stockData.getFloorPrice()));
                    company.setRefPrice(DataUtil.safeToDouble(stockData.getRefPrice()));
                    company.setLastVolume(DataUtil.safeToLong(stockData.getLastVolume()));
                    company.setHighPrice(DataUtil.safeToDouble(stockData.getHighPrice()));
                    company.setLowPrice(DataUtil.safeToDouble(stockData.getLowPrice()));
                }
            });
        }
        return companies;
    }

    private Map<String, SymbolData> getStockDataFromCore(Set<String> stockCodes) {
        String listStockCodes = String.join(SPLIT_COMMA, stockCodes);
        List<SymbolData> stockData = polaServiceHelper.getStockData(listStockCodes);
        Map<String, SymbolData> mapStockCore = stockData.stream()
                .collect(Collectors.toMap(SymbolData::getStockCode, Function.identity()));
        return mapStockCore;
    }

    private void saveCustomerWatchlistStocks(String stocks, Long idWatchlist) throws BusinessException {
        List<String> stockCodes = Arrays.asList(stocks.split(SPLIT_COMMA));
        List<String> existedWatchlistStocks = watchlistStockRepository.findAllWatchlistStockByIdAndStocks(idWatchlist, stockCodes);
        List<WatchlistStock> watchlistStockList = stockCodes.stream()
                .filter(stockCode -> !existedWatchlistStocks.contains(stockCode))
                .map(stockCode -> WatchlistStock.builder()
                        .watchlistId(idWatchlist)
                        .stockCode(stockCode)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        List<WatchlistStock> savedWatchlistStocks = watchlistStockRepository.saveAll(watchlistStockList);

        watchlistKafkaTemplate.send(PineKafkaProperties.TOPIC_WATCHLIST, WatchlistMessage.builder()
                .customerId(getCurrentCustomerId())
                .list(savedWatchlistStocks.stream()
                        .map(watchlistStock -> WatchlistMessage.ItemInfo.builder()
                                .stockCode(watchlistStock.getStockCode())
                                .watch(true)
                                .build())
                        .collect(Collectors.toList()))
                .timestamp(System.currentTimeMillis())
                .build());
    }

    private void validateOwner(Watchlist watchlist) throws BusinessException {
        if (getAuthentication() == null || !getAuthentication().getUserId().equals(watchlist.getCustomerId())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, Message.Error.PERMISSION_DENIED);
        }
    }

    private Watchlist findWatchlistById(Long watchlistId) throws BusinessException {
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(watchlistId);
        if (!optionalWatchlist.isPresent()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.Error.WATCHLIST_NOTFOUND);
        }
        return optionalWatchlist.get();
    }

    private Watchlist findWatchlistByIdAndIdCustomer(Long idCustomer, Long watchlistId) throws BusinessException {
        Watchlist watchlist = watchlistRepository.findByCustomerIdAndId(idCustomer, watchlistId);
        if (watchlist == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, Message.Error.WATCHLIST_NOTFOUND);
        }
        return watchlist;
    }

    public ResponseDTOCursor<List<TopWatchingStockDTO>> getTopWatchingStock(String cursor, int limit) throws BusinessException {
        List<TopWatchingStockDTO> topWatchingStockDTOS = new ArrayList<>();
        if (!DataUtil.isNullOrEmpty(cursor)) {
            //search with cursor
            String[] cursorSplitted = cursor.split("_");
            if (cursorSplitted.length < 2) {
                return ResponseDTOCursor.<List<TopWatchingStockDTO>>builder()
                        .data(topWatchingStockDTOS)
                        .build();
            }
            int count = DataUtil.safeToInt(cursorSplitted[0]);
            String stockCodeCurSor = cursorSplitted[1];
            topWatchingStockDTOS = watchlistStockRepository.findTopWatchingStockPaging(count, stockCodeCurSor, limit);

        } else {
            topWatchingStockDTOS = watchlistStockRepository.findTopWatchingStock(limit);
        }
        Set<String> stockCodes = topWatchingStockDTOS.stream().map(TopWatchingStockDTO::getStockCode).collect(Collectors.toSet());
        // TODO: call to market service after deploying
//        List<CompanyResponse> companies = marketServiceHelper.getCompaniesByCodes(stockCodes);
        List<CompanyResponse> companies = marketServiceHelper.getCompaniesByCodes(stockCodes);

        Map<String, CompanyResponse> companyShortInfoMap = companies.stream()
                .collect(Collectors.toMap(CompanyResponse::getStockCode, Function.identity()));
        topWatchingStockDTOS.forEach(t -> {
            CompanyResponse companyShortInfo = companyShortInfoMap.get(t.getStockCode());
            if (companyShortInfo == null) {
                log.error("missing stock from market: {}", t.getStockCode());
                return;
            }
            t.setCompanyName(companyShortInfo.getName());
            t.setStockExchange(companyShortInfo.getStockExchange().name());
        });
        String newCursor = "";
        if (!DataUtil.isNullOrEmpty(topWatchingStockDTOS)) {
            TopWatchingStockDTO lastItem = topWatchingStockDTOS.get(topWatchingStockDTOS.size() - 1);
            newCursor = lastItem.getTotalCount() + "_" + lastItem.getStockCode();
        }

        return ResponseDTOCursor.<List<TopWatchingStockDTO>>builder()
                .data(topWatchingStockDTOS)
                .cursor(newCursor)
                .build();
    }

    @Override
    public List<StockCodeSuggestionResponse> getWatchlistRecommendation(int limit) throws BusinessException {
        try {
            List<WatchlistStock> listStockWatchedByUser = watchlistStockRepository.findAllWatchListStockByCustomerId(getAuthentication().getUserId());
            //If list size == 0. Get default by topWatching
            List<String> stockCodeListWatchedByUser;
            if (listStockWatchedByUser.size() == 0) {
                List<TopWatchingStockDTO> topWatchingStockDTOS = watchlistStockRepository.findTopWatchingStock(limit);
                stockCodeListWatchedByUser = topWatchingStockDTOS.stream().map(TopWatchingStockDTO::getStockCode).collect(Collectors.toList());
            } else {
                stockCodeListWatchedByUser = listStockWatchedByUser.stream().map(WatchlistStock::getStockCode).collect(Collectors.toList());
            }

            List<StockSuggestionResponse> stockCodeListInSameCompanyGroup = marketServiceHelper.getStockCodeInSameCompanyGroup(stockCodeListWatchedByUser, limit);

            List<SymbolData> symbolDataList = getSymbolDataFromWTS(stockCodeListInSameCompanyGroup);

            Map<String, SymbolData> mapSymbolDataMap = symbolDataList.stream().collect(Collectors.toMap(SymbolData::getStockCode, Function.identity()));
            List<StockCodeSuggestionResponse> stockCodeSuggestionDtos = new ArrayList<>();
            SymbolData currentSymbolData;
            for (StockSuggestionResponse stockSuggestionResponse : stockCodeListInSameCompanyGroup) {
                if (stockCodeListWatchedByUser.contains(stockSuggestionResponse.getStockCode())) {
                    log.debug("This stock code already watched by this user: {}", stockSuggestionResponse.getStockCode());
                    continue;
                }
                currentSymbolData = mapSymbolDataMap.get(stockSuggestionResponse.getStockCode());
                boolean isVN30 = stockSuggestionResponse.getIsVn30();
                boolean isHNX30 = stockSuggestionResponse.getIsHnx30();
                int score = Constant.STOCK_CODE_SUGGESTION_IN_SAME_TYPE_POINT;
                if (isVN30 || isHNX30) {
                    log.debug("this stock code belong N30/NHX30 : {}", stockSuggestionResponse.getStockCode());
                    score += Constant.STOCK_CODE_SUGGESTION_IN_VN30_OR_HNX30_POINT;
                }
                if (stockSuggestionResponse.getRoaYear() > 0 || stockSuggestionResponse.getRoeYear() > 0) {
                    log.debug("this stock code have roa/roe positive : {}", stockSuggestionResponse.getStockCode());
                    score += Constant.STOCK_CODE_SUGGESTION_HAVE_POSITIVE_ROA_ROE_POINT;
                }
                try {
                    StockInfo stockInfo = getStockInfo(stockSuggestionResponse);
                    if (!DataUtil.isNullObject(stockInfo)) {
                        if (stockInfo.getPerChange() > 0) {
                            log.debug("this stock code have positive perChange : {}", stockSuggestionResponse.getStockCode());
                            score += Constant.STOCK_CODE_SUGGESTION_IN_POSITIVE_PER_CHANGE_POINT;
                        }
                    }
                } catch (BusinessException e) {
                    log.error("call wts to get stock perChange error");

                }
                StockCodeSuggestionResponse stockCodeSuggestionResponse = objectMapper.convertFromStockSuggestionResponse(stockSuggestionResponse);
                buildStockCodeSuggestionResponse(currentSymbolData, score, stockCodeSuggestionResponse);
                stockCodeSuggestionDtos.add(stockCodeSuggestionResponse);
            }
            List<StockCodeSuggestionResponse> lst = stockCodeSuggestionDtos.stream()
                    .sorted(Comparator.comparingInt(StockCodeSuggestionResponse::getScore).reversed())
                    .collect(Collectors.toList());
            if (limit == 0)
                return lst;
            else {
                if (limit > lst.size()) return lst;
                else return lst.subList(0, limit);
            }
        } catch (BusinessException e) {
            log.error("Error when getWatchlistRecommendation: ", e);
            return new ArrayList<>();
        }
    }

    private StockInfo getStockInfo(StockSuggestionResponse stockSuggestionResponse) throws BusinessException {
        return polaServiceHelper.getStockInfo(stockSuggestionResponse.getStockCode());
    }

    private void buildStockCodeSuggestionResponse(SymbolData currentSymbolData, int score, StockCodeSuggestionResponse stockCodeSuggestionResponse) {
        try {
            stockCodeSuggestionResponse.setScore(score);
            stockCodeSuggestionResponse.setRefPrice(currentSymbolData.getRefPrice());
            stockCodeSuggestionResponse.setCeilPrice(currentSymbolData.getCeilPrice());
            stockCodeSuggestionResponse.setFloorPrice(currentSymbolData.getFloorPrice());
            stockCodeSuggestionResponse.setLastPrice(currentSymbolData.getLastPrice());
            stockCodeSuggestionResponse.setLastVolume(currentSymbolData.getLastVolume());
            stockCodeSuggestionResponse.setLastVolume(currentSymbolData.getLastVolume());
            stockCodeSuggestionResponse.setChange(Double.parseDouble(currentSymbolData.getChangeValue()));
            stockCodeSuggestionResponse.setPerChange(Double.parseDouble(currentSymbolData.getChangePc()));
            stockCodeSuggestionResponse.setHighPrice(Double.parseDouble(currentSymbolData.getHighPrice()));
            stockCodeSuggestionResponse.setLowPrice(Double.parseDouble(currentSymbolData.getLowPrice()));
        } catch (Exception ex) {
            log.error("buildStockCodeSuggestionResponse error: " + ex.getMessage());
        }
    }

    private List<SymbolData> getSymbolDataFromWTS(List<StockSuggestionResponse> stockCodeListInSameCompanyGroup) {
        Set<String> stockCodes = stockCodeListInSameCompanyGroup.stream().map(StockSuggestionResponse::getStockCode).collect(Collectors.toSet());
        String listStockCodes = String.join(SPLIT_COMMA, stockCodes);
        List<SymbolData> symbolDataList = polaServiceHelper.getStockData(listStockCodes);
        return symbolDataList;
    }
}
