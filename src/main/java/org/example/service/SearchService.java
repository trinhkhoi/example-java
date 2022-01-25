package org.example.service;

import org.example.dto.request.CustomerSearchRequest;
import org.example.dto.response.SearchResponse;
import org.common.dto.response.dw.StockIPO;
import org.common.exception.BusinessException;

import java.util.List;

public interface SearchService {
    List<StockIPO> getTopStockNewListed() throws BusinessException;
    SearchResponse search(CustomerSearchRequest request) throws BusinessException;
}
