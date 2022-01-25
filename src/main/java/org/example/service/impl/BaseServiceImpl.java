package org.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.common.exception.BusinessException;
import org.common.helper.BaseServiceImplCommon;
import org.common.utils.DateUtil;
import org.example.service.ParameterService;
import org.example.util.Constant;

import java.time.LocalDate;

@Component
@Service
public class BaseServiceImpl extends BaseServiceImplCommon {
    @Autowired
    private ParameterService parameterService;

    public boolean isGivenDateBeforeChangeAmountDate(LocalDate someDate) {
        String changeDateStr;
        try {
            changeDateStr = parameterService.getValueByKey(Constant.DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100);
        } catch (BusinessException e) {
            changeDateStr = Constant.DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100_DEFAULT;
        }
        LocalDate changeDate = DateUtil.stringToT24LocalDate(changeDateStr);
        return someDate.isBefore(changeDate);
    }

    public boolean isGivenDateIsEqualOrAfterChangeAmountDate(LocalDate someDate) {
        String changeDateStr;
        try {
            changeDateStr = parameterService.getValueByKey(Constant.DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100);
        } catch (BusinessException e) {
            changeDateStr = Constant.DAY_CHANGE_COUPON_AWARD_FROM_200_TO_100_DEFAULT;
        }
        LocalDate changeDate = DateUtil.stringToT24LocalDate(changeDateStr);
        return someDate.isAfter(changeDate) || someDate.equals(changeDate);
    }

}
