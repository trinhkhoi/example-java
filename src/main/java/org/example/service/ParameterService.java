package org.example.service;

import org.common.exception.BusinessException;

import java.util.List;

public interface ParameterService {
    List<String> getSuggestedStockCodes() throws BusinessException;

    List<String> getPermisBuyingStockCodes() throws BusinessException;

    int getNumberValidDayForCoupon() throws BusinessException;

    int getMaximumCouponPerUser() throws BusinessException;

    double getCouponAmountForNewComer() throws BusinessException;

    double getCouponAmountForReferral() throws BusinessException;

    double getCouponAmountForSharingPortfolio() throws BusinessException;

    double getPointByType(String type) throws BusinessException;

    int getDayCanChangeSetting() throws BusinessException;

    String getValueByKey(String key) throws BusinessException;
}