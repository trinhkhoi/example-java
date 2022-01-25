package org.example.repository;

import org.example.entity.ThemeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ThemeDetailRepository extends JpaRepository<ThemeDetail, Long> {

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE theme_detail SET deleted_at = NOW() WHERE theme_code = ?1")
    void deleteThemeDetailByThemeCode(String themeCode);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE theme_detail SET deleted_at = NOW() WHERE stock_code = ?1")
    void deleteThemeDetailByStockCode(String stockCode);

    List<ThemeDetail> findAllByThemeCode(String themeCode);

    @Query(nativeQuery = true, value = "SELECT * FROM theme_detail WHERE theme_code in (?1) AND deleted_at IS NULL")
    List<ThemeDetail> findAllByListThemeCode(List<String> themeCodes);
}
