package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.example.dto.TopWatchingStockDTO;
import org.example.entity.WatchlistStock;

import java.util.List;

@Repository
public interface WatchlistStockRepository extends JpaRepository<WatchlistStock, Long> {
    void deleteAllByWatchlistId(Long watchlistId);

    List<WatchlistStock> findAllByWatchlistId(Long watchlistId);

    WatchlistStock findByWatchlistIdAndStockCode(Long watchlistId, String stockCode);

    @Query(nativeQuery = true)
    List<TopWatchingStockDTO> findTopWatchingStock(int limit);

    @Query(nativeQuery = true)
    List<TopWatchingStockDTO> findTopWatchingStockPaging(int count, String stockCode, int limit);

    @Query(nativeQuery = true, value = "select stock_code from watchlist_stock where watchlist_id = ?1 and stock_code in (?2) and deleted_at is null")
    List<String> findAllWatchlistStockByIdAndStocks(Long watchlistId, List<String> stocks);

    @Query(nativeQuery = true, value = "select count(*) as numberOfRecord from watchlist_stock where watchlist_id in (select watchlist.id from watchlist where customer_id=?1) and deleted_at is null")
    int countStockWatchlistByCustomerId(Long customerId);

    @Query(nativeQuery = true, value = "select * from watchlist_stock ws where ws.watchlist_id in (select w.id from watchlist w where w.customer_id=?1) and deleted_at is null")
    List<WatchlistStock> findAllWatchListStockByCustomerId(Long customerId);
}
