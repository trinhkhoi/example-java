package org.example.entity;

import org.apache.commons.lang3.StringUtils;
import org.example.dto.response.CompanyKeywordSolrResponse;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;

/**
 * solr collection
 * @author nguyentienthanh
 */
@SolrDocument(collection = "search_all")
public class CompanyKeywordSolr {

    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Indexed(name = "code", type = "string")
    private String code;

    @Indexed(name = "name", type = "string")
    private String name;

    @Indexed(name = "short_name", type = "string")
    private String shortName;

    @Indexed(name = "introduction", type = "string")
    private String introduction;

    @Indexed(name = "stock_exchange", type = "string")
    private String stockExchange;

    @Indexed(name = "search_type", type = "string")
    private String searchType;

    @Indexed(name = "margin", type = "string")
    private String margin;

    @Indexed(name = "has_news", type = "string")
    private String hasNews;

    public CompanyKeywordSolr() {
    }

    public CompanyKeywordSolrResponse getCompanyKeywordSolrResponse() {
        return new CompanyKeywordSolrResponse(this.name, this.code, this.stockExchange, this.searchType, this.introduction, this.shortName, getMarginDisplay(), getHasNewsDisplay());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getMargin() {
        return margin;
    }

    public String getMarginDisplay() {
        return StringUtils.isBlank(margin) ? "0" : margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getHasNews() {
        return hasNews;
    }

    public Boolean getHasNewsDisplay() {
        return hasNews.equals("1");
    }

    public void setHasNews(String hasNews) {
        this.hasNews = hasNews;
    }
}
