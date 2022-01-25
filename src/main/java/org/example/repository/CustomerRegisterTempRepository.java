package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.entity.CustomerRegisterTemp;

@Repository
public interface CustomerRegisterTempRepository extends JpaRepository<CustomerRegisterTemp, Long> {

    CustomerRegisterTemp findByPhone(String phone);

    CustomerRegisterTemp findByOtp(String otp);
}
