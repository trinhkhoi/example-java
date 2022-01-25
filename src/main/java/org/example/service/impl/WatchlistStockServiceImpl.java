package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.WatchlistStock;
import org.example.repository.WatchlistStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.service.WatchlistStockService;

import java.util.List;

@Service
@Slf4j
public class WatchlistStockServiceImpl implements WatchlistStockService {
    @Autowired
    private WatchlistStockRepository watchlistStockRepository;

    @Override
    public int countStockWatchlistByCustomerId(Long customerId) {
        return watchlistStockRepository.countStockWatchlistByCustomerId(customerId);
    }

    @Override
    public List<WatchlistStock> findAllWatchListStockByCustomerId(Long customerId) {
        return watchlistStockRepository.findAllWatchListStockByCustomerId(customerId);
    }
}
