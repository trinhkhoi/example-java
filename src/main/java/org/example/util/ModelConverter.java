package org.example.util;

import org.example.entity.Customer;
import org.common.dto.kafka.CustomerMessage;
import org.common.dto.kafka.CustomerSettingMessage;

public class ModelConverter {
    public static CustomerMessage buildKafkaCustomerMessage(Customer customer) {
        return CustomerMessage.builder()
                .customerId(customer.getId())
                .avatar(customer.getAvatar())
                .name(customer.getName())
                .displayName(customer.getDisplayName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhone())
                .cif(customer.getCif())
                .guid(customer.getGuid())
                .custStat(customer.getCustStat())
                .acntStat(customer.getAcntStat())
                .accountNo(customer.getAccountNo())
                .subAccount(customer.getSubAccount())
                .fcmToken(customer.getFcmToken())
                .vsd(customer.getVsd())
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private ModelConverter() {
    }
}
