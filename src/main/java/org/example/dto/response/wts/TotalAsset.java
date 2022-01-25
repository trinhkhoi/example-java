package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TotalAsset {

    private Double wdrawAvail;
    private Double totAsst;
    private Double realAsst;
    private Double balance;
    private Double advanceAvail;
    private Double advanceLoan;
    private Double buyT0;
    private Double tavlStockValue;
    private Double ptavlStockValue;
    private Double tartStockValue;
    private Double ptartStockValue;
    private Double righStockValue;
    private Double rcvStockValue;
    private Double sellT0;
    private Double mgDebt;
    private Double prinDebt;
    private Double intDebt;
    private Double fee;
    private Double depoFee;
    private Double smsFee;
    private Double pp0;
    private Double marginRatio;
    private Double othrFee;
    private Double tdtBuyAmtNotMatch;
    private Double cash;
    private Double couponCash;
    private Double receiveAmt;
    private Double cashDiv;
    private Double stockValue;
    private Double totalStock;
    private Double debt;
    private Double exptDisbm;

    public Double getCouponCash() {
        return couponCash == null ? 0.0 : couponCash;
    }
}
