package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.example.entity.CustomerRegister;

@Repository
public interface CustomerRegisterRepository extends JpaRepository<CustomerRegister, Long> {

    CustomerRegister findByPhone(String phone);

    @Modifying
    @Transactional(noRollbackFor = Exception.class)
    @Query(nativeQuery = true, value = "UPDATE customer_register SET cif = ?2 WHERE id = ?1")
    void updateCif(Long id, String cif);
}
