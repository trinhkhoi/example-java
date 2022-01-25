package org.example.repository;

import org.example.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(value = "UPDATE Theme t SET t.totalSubsribe = t.totalSubsribe + 1, t.updatedAt = current_timestamp WHERE t.themeCode = :themeCode")
    void increaseTotalSubscribe(@Param("themeCode") final String themeCode);

    Theme findByThemeCode(String themeCode);

    @Query(nativeQuery = true, value = "SELECT * FROM theme WHERE theme_code IN (?1) AND deleted_at IS NULL")
    List<Theme> findByListThemeCode(List<String> themeCodes);

    @Query(nativeQuery = true, value = "SELECT t.* FROM theme t INNER JOIN theme_detail td ON t.theme_code = td.theme_code AND t.deleted_at IS NULL " +
            "WHERE td.stock_code IN (?1) AND td.deleted_at IS NULL")
    List<Theme> findAllThemeByStockCode(List<String> stockCodes);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query("UPDATE Theme SET deletedAt = current_timestamp WHERE themeCode = :themeCode")
    void deleteTheme(@Param("themeCode") final String themeCode);
}
