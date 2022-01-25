package org.example.service;

import org.common.exception.BusinessException;
import org.example.dto.request.RegisterCustomerTempRequest;

public interface CustomerRegisterTempService {

    void createNewPistCustomer(RegisterCustomerTempRequest request) throws BusinessException;

    void verifyEmailOtp(String otp) throws BusinessException;

}
