package org.example.repository;

import org.example.entity.CustomerTheme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomerThemeRepository extends JpaRepository<CustomerTheme, Long> {
    Long countAllByThemeCode(String themeCode);

    Page<CustomerTheme> findAllByThemeCodeOrderByCreatedAtDesc(String themeCode, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from customer_theme where theme_code = ?1 and deleted_at is null order by id DESC LIMIT ?2")
    List<CustomerTheme> findAllByThemeCodeFirstPage(String themeCode, int limit);

    @Query(nativeQuery = true, value = "select * from customer_theme where theme_code = ?1 and id < ?2 and deleted_at is null order by id DESC LIMIT ?3")
    List<CustomerTheme> findAllByThemeCode(String themeCode, Long lastCustomerThemeId, int limit);

    @Query(nativeQuery = true, value = "SELECT id FROM customer_theme WHERE id_customer = ?1 and theme_code = ?2 and deleted_at is null")
    Long findIdByCustomerAndTheme(Long idCustomer, String themeCode);

    @Query(nativeQuery = true, value = "SELECT theme_code FROM customer_theme WHERE id_customer = ?1 and deleted_at is null")
    List<String> findListCustomerThemes(Long idCustomer);

    @Query(nativeQuery = true, value = "SELECT theme_code FROM customer_theme WHERE id_customer = ?1 and theme_code in (?2) and deleted_at is null")
    List<String> findListIdByCustomerAndTheme(Long idCustomer, List<String> themeCodes);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE customer_theme SET deleted_at = NOW() WHERE id_customer = ?1 and theme_code = ?2")
    void deleteByIdCustomerAndThemeCode(Long idCustomer, String themeCode);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE customer_theme SET deleted_at = NOW() WHERE id_customer = ?1 and theme_code in (?2)")
    void deleteByIdCustomerAndThemeCodes(Long idCustomer, List<String> themeCodes);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE customer_theme SET deleted_at = NOW() WHERE theme_code = ?1")
    void deleteByThemeCode(String themeCode);
}
