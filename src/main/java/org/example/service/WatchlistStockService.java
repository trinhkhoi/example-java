package org.example.service;

import org.example.entity.WatchlistStock;

import java.util.List;

public interface WatchlistStockService {
    int countStockWatchlistByCustomerId(Long customerId);

    List<WatchlistStock> findAllWatchListStockByCustomerId(Long customerId);
}
