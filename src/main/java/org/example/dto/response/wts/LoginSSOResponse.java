package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.example.util.Constant;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginSSOResponse {
    private String msg;
    private int rc;
    private CustomerInfo custInfo;
    private Data data;
    private String vsd;
    private String custNm;
    private String perdomAddr;
    private String cttAddr;
    private String brthdt;
    private String gend;
    private String custId;
    private String custIdKnd;
    private int http;
    private Config config;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Config {
        private int firstLogin;
        private int maxDay;
        private String morePrice;
        private Object minAdv;
        private Constant.AuthDef authDef;
        private int numDay;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerInfo {
        private String cif;
        private List<SubAccountInfo> normal;
        private Object fund;
        private String bizDate;
        private String sysTime;
        private Object otc;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String cif;
        private String custStat;
        private String session;
        private String mobile;
        private String email;
        private String opndt;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubAccountInfo {
        private String subAcntNo;
        private String vsdAcntNo;
        private String acntNo;
        private int subAcntStat;
        private int acntStat;
        private int isDefSubAcnt;
        private String opnDt;
    }
}
