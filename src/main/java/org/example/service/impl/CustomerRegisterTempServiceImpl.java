package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.CustomerRegisterTemp;
import org.example.repository.CustomerRegisterTempRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.common.utils.DateUtil;
import org.example.dto.request.RegisterCustomerTempRequest;
import org.example.service.CustomerRegisterTempService;
import org.example.util.CustomerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@Slf4j
public class CustomerRegisterTempServiceImpl implements CustomerRegisterTempService {
    private static Logger logger = LoggerFactory.getLogger(CustomerRegisterTempServiceImpl.class);
    @Autowired
    private CustomerRegisterTempRepository customerRegisterTempRepository;
    @Autowired
    private CustomerUtil customerUtil;

    @Override
    public void createNewPistCustomer(RegisterCustomerTempRequest request) throws BusinessException {
        CustomerRegisterTemp existedRegisterCustomer = customerRegisterTempRepository.findByPhone(request.getPhone());
        if (existedRegisterCustomer == null) {
            String defaultAvatar = "";
            if (StringUtils.isBlank(request.getDisplayName())) {
                defaultAvatar = request.getDisplayName();
            } else if (StringUtils.isBlank(request.getEmail())) {
                defaultAvatar = request.getEmail();
            }

            existedRegisterCustomer = CustomerRegisterTemp.builder()
                    .phone(request.getPhone())
                    .address(request.getAddress())
                    .city(request.getCity())
                    .dob(DateUtil.stringToT24LocalDate(request.getDob()))
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .displayName(StringUtils.isBlank(request.getDisplayName()) ? request.getFullName() : request.getDisplayName())
                    .avatar(customerUtil.setDefaultAvatar(defaultAvatar))
                    .gender(request.getGender())
                    .identityNo(request.getIdentityNo())
                    .isAcceptCondition(request.getIsAcceptCondition())
                    .issueBy(request.getIssueBy())
                    .issueDate(DateUtil.stringToT24LocalDate(request.getIssueDate()))
                    .userName(request.getUserName())
                    .isVerifyEmail(Boolean.FALSE)
                    .numberSent(1)
                    .createdAt(now())
                    .updatedAt(now())
                    .build();
            customerRegisterTempRepository.save(existedRegisterCustomer);
        }

    }

    @Override
    public void verifyEmailOtp(String otp) throws BusinessException {
        CustomerRegisterTemp customerRegisterTemp = customerRegisterTempRepository.findByOtp(otp);
        if (customerRegisterTemp == null) {
            throw new BusinessException("Tài khoản này không chính xác");
        }
        customerRegisterTemp.setIsVerifyEmail(Boolean.TRUE);
        customerRegisterTempRepository.save(customerRegisterTemp);
    }
}
