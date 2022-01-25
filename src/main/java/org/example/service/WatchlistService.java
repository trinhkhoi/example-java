package org.example.service;

import org.common.dto.response.ResponseDTOCursor;
import org.common.exception.BusinessException;
import org.example.dto.StockCodeSuggestionResponse;
import org.example.dto.TopWatchingStockDTO;
import org.example.dto.request.CreateUpdateWatchlistRequest;
import org.example.dto.response.CompanyResponse;
import org.example.dto.response.WatchlistResponse;
import org.example.entity.Watchlist;

import java.util.List;

public interface WatchlistService {
    Watchlist createWatchlist(CreateUpdateWatchlistRequest request) throws BusinessException;

    Watchlist updateWatchlist(Long watchlistId, CreateUpdateWatchlistRequest request) throws BusinessException;

    void shareWatchlist(Long idCustomer, boolean isShare) throws BusinessException;

    void removeStockFromWatchlist(String stockCode) throws BusinessException;

    List<CompanyResponse> getWatchlistStocks(Long watchlistId) throws BusinessException;

    List<WatchlistResponse> getWatchlistList() throws BusinessException;

    List<WatchlistResponse> getWatchlistListOtherCustomer(Long idCustomer) throws BusinessException;

    ResponseDTOCursor<List<TopWatchingStockDTO>> getTopWatchingStock(String cursor, int limit) throws BusinessException;

    List<StockCodeSuggestionResponse> getWatchlistRecommendation(int limit) throws BusinessException;
}
