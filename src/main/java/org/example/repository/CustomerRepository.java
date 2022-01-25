package org.example.repository;

import org.example.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerRepositoryCustom {
    Customer findByCif(String cif);

    List<Customer> findAllByCifIn(List<String> cifs);

    List<Customer> findAllByIdIsIn(List<Long> ids);

    @Query(nativeQuery = true, value = "SELECT * FROM customer WHERE phone = ?1 AND deleted_at IS NULL")
    Customer findByPhone(String phoneNumber);

    @Query(nativeQuery = true, value = "SELECT c.* FROM customer c INNER JOIN (SELECT ct.id_customer AS id_customer FROM customer_theme ct WHERE ct.id_customer <> ?1 AND ct.theme_code <> ?2 AND ct.deleted_at IS NULL ORDER BY created_at LIMIT 3) as a ON c.id = a.id_customer")
    List<Customer> getLatestSubscribeThemeWithLimit(Long idCustomer, String themeCode);

    List<Customer> findAllByDisplayNameContaining(String keyword);

    @Query(nativeQuery = true, value = "SELECT * FROM customer c" +
            " INNER JOIN customer_friend cf" +
            " ON c.id = cf.id_customer" +
            " AND cf.deleted_at IS NULL" +
            " WHERE cf.id_friend = ?1 AND c.deleted_at IS NULL" +
            " ORDER BY cf.created_at LIMIT 3;")
    List<Customer> getLatestFollowerFriend(Long idCustomer);

    List<Customer> findAllByIsFeatureProfileTrueOrIsKolTrue();
}
