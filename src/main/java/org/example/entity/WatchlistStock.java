package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.example.dto.TopWatchingStockDTO;

import javax.persistence.*;


@SqlResultSetMappings({
        @SqlResultSetMapping(name = "TopWatchingStockDTO",
                classes = {
                        @ConstructorResult(targetClass = TopWatchingStockDTO.class,
                                columns = {
                                        @ColumnResult(name = "stock_code", type = String.class),
                                        @ColumnResult(name = "total_count", type = Integer.class)
                                })
                }
        )
})

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "WatchlistStock.findTopWatchingStock",
                query = "select stock_code , COUNT(id) as total_count from pist.watchlist_stock ws " +
                        "group by stock_code order by total_count DESC, stock_code  LIMIT ?1",
                resultSetMapping = "TopWatchingStockDTO"),
        @NamedNativeQuery(
                name = "WatchlistStock.findTopWatchingStockPaging",
                query = "select stock_code , COUNT(id) as total_count from pist.watchlist_stock ws " +
                        "group by stock_code HAVING ((total_count <= ?1 AND stock_code > ?2) OR (total_count < ?1))  order by total_count DESC , stock_code  LIMIT ?3",
                resultSetMapping = "TopWatchingStockDTO")
})

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "watchlist_stock")
@Where(clause = "deleted_at IS NULL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WatchlistStock extends BaseEntity {
    @Column(name = "watchlist_id", nullable = false, columnDefinition = "bigint")
    private Long watchlistId;

    @Column(name = "stock_code", columnDefinition = "varchar(255)")
    private String stockCode;
}
