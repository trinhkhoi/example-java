package org.example.dto.response.wts;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SymbolData {
    @JsonAlias("sym")
    @JsonProperty("stockCode")
    private String stockCode;
    @JsonAlias("name")
    @JsonProperty("stockName")
    private String stockName;
    @JsonAlias("c")
    @JsonProperty("ceilPrice")
    private Double ceilPrice;
    @JsonAlias("r")
    @JsonProperty("refPrice")
    private Double refPrice;
    @JsonAlias("f")
    @JsonProperty("floorPrice")
    private Double floorPrice;
    @JsonAlias("lastPrice")
    @JsonProperty("lastPrice")
    private Double lastPrice;
    @JsonAlias("lastVolume")
    @JsonProperty("lastVolume")
    private Long lastVolume;
    @JsonAlias("changePc")
    @JsonProperty("changePc")
    private String changePc;
    @JsonAlias("ot")
    @JsonProperty("changeValue")
    private String changeValue;
    @JsonAlias("highPrice")
    @JsonProperty("highPrice")
    private String highPrice;
    @JsonAlias("lowPrice")
    @JsonProperty("lowPrice")
    private String lowPrice;
}
