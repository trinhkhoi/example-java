package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.entity.Watchlist;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Optional<Watchlist> findFirstByCustomerId(Long customerId);

    List<Watchlist> findAllByCustomerId(Long customerId);

    Watchlist findByCustomerIdAndId(Long customerId, Long Id);
}
