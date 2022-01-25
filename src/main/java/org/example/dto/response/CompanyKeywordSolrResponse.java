package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyKeywordSolrResponse {
    private String name;
    private String stockCode;
    private String stockExchange;
    private String searchType;
    private String introduction;
    private String shortName;
    private String margin;
    private Boolean hasNews;

}
