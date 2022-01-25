package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockInfo {
    @JsonProperty("TradingDate")
    public Date tradingDate;
    @JsonProperty("KLCPLH")
    public double kLCPLH;
    @JsonProperty("KLCPNY")
    public double kLCPNY;
    @JsonProperty("TotalVol")
    public double totalVol;
    @JsonProperty("TotalVal")
    public double totalVal;
    @JsonProperty("MarketCapital")
    public double marketCapital;
    @JsonProperty("LastPrice")
    public double lastPrice;
    @JsonProperty("Change")
    public double change;
    @JsonProperty("PerChange")
    public double perChange;
    @JsonProperty("Min52W")
    public double min52W;
    @JsonProperty("Max52W")
    public double max52W;
    @JsonProperty("Vol52W")
    public double vol52W;
    @JsonProperty("OutstandingBuy")
    public double outstandingBuy;
    @JsonProperty("OutstandingSell")
    public double outstandingSell;
    @JsonProperty("OwnedRatio")
    public double ownedRatio;
    @JsonProperty("Dividend")
    public double dividend;
    @JsonProperty("Yield")
    public double yield;
    @JsonProperty("Beta")
    public double beta;
    @JsonProperty("EPS")
    public double ePS;
    @JsonProperty("PE")
    public double pE;
    @JsonProperty("FEPS")
    public double fEPS;
    @JsonProperty("BVPS")
    public double bVPS;
    @JsonProperty("PB")
    public double pB;
    @JsonProperty("CurrRoom")
    public double currRoom;
    @JsonProperty("F_BuyVol")
    public double f_BuyVol;
    @JsonProperty("F_SellVol")
    public double f_SellVol;
    @JsonProperty("MarketStatus")
    public int marketStatus;
    @JsonProperty("TotalAdjustRate")
    public double totalAdjustRate;
}
