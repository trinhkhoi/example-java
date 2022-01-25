package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.Parameter;
import org.example.repository.ParameterRepository;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.common.utils.DataUtil;
import org.example.dto.mapper.ParamMapper;
import org.example.service.ParameterService;
import org.example.util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParameterServiceImpl implements ParameterService {
    private final ParameterRepository parameterRepository;
    private final ParamMapper mapper;

    @Override
    public List<String> getSuggestedStockCodes() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode(Constant.PARAM_SUGGESTED_STOCK_CODES);
        if (StringUtils.isBlank(parameter.getDescription())) {
            return new ArrayList<>();
        }
        return Arrays.asList(parameter.getDescription().split(","));
    }

    @Override
    public List<String> getPermisBuyingStockCodes() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode(Constant.PARAM_PERMISSION_STOCK_CODES_BUYING);
        if (StringUtils.isBlank(parameter.getDescription())) {
            return new ArrayList<>();
        }
        return Arrays.asList(parameter.getDescription().split(","));
    }


    @Override
    public int getNumberValidDayForCoupon() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode("COUPON_EXPIRE_DAY");
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.COUPON_EXPIRE_DAY_DEFAULT;
        }
        int ret = DataUtil.safeToInt(parameter.getDescription());

        if (ret < 1) {
            ret = Constant.COUPON_EXPIRE_DAY_DEFAULT;
        }
        return ret;
    }

    @Override
    public int getMaximumCouponPerUser() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode("COUPON_MAXIMUM_PER_USER");
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.COUPON_MAXIMUM_PER_USER_DEFAULT;
        }
        int ret = DataUtil.safeToInt(parameter.getDescription());

        if (ret < 3) {
            ret = Constant.COUPON_MAXIMUM_PER_USER_DEFAULT;
        }
        return ret;
    }


    @Override
    public double getCouponAmountForNewComer() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode("COUPON_AMOUNT_NEW_COMER");
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.COUPON_AMOUNT_DEFAULT;
        }
        double ret = DataUtil.safeToDouble(parameter.getDescription());

        if (ret < 1000) {
            ret = Constant.COUPON_AMOUNT_DEFAULT;
        }
        return ret;
    }

    @Override
    public double getCouponAmountForReferral() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode("COUPON_AMOUNT_REFERRAL");
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.COUPON_AMOUNT_DEFAULT;
        }
        double ret = DataUtil.safeToDouble(parameter.getDescription());

        if (ret < 1000) {
            ret = Constant.COUPON_AMOUNT_DEFAULT;
        }
        return ret;
    }

    @Override
    public double getCouponAmountForSharingPortfolio() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode("COUPON_AMOUNT_SHARE_PORTFOLIO");
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.COUPON_AMOUNT_DEFAULT;
        }
        double ret = DataUtil.safeToDouble(parameter.getDescription());

        if (ret < 1000) {
            ret = Constant.COUPON_AMOUNT_DEFAULT;
        }
        return ret;
    }

    @Override
    public double getPointByType(String type) throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode(type);
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.KOL_POINT_DEFAULT;
        }
        return DataUtil.safeToDouble(parameter.getDescription());
    }

    @Override
    public int getDayCanChangeSetting() throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode("COUPON_DAY_CAN_CHANGE_SETTING_AFTER_SHARE_POR");
        if (StringUtils.isBlank(parameter.getDescription())) {
            return Constant.COUPON_DAY_CAN_CHANGE_SETTING_DEFAULT;
        }
        return DataUtil.safeToInt(parameter.getDescription());
    }

    @Override
    public String getValueByKey(String key) throws BusinessException {
        Parameter parameter = parameterRepository.findByParamCode(key);
        if (parameter == null) {
            throw new BusinessException(key + " was not found");
        }
        return parameter.getDescription();
    }
}
