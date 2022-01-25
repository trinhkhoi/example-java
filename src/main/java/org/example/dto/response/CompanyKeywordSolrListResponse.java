package org.example.dto.response;

import org.example.dto.response.parent.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class CompanyKeywordSolrListResponse extends SearchResult {

    private List<CompanyKeywordSolrResponse> content = new ArrayList<>();

    public CompanyKeywordSolrListResponse() {
    }

    public CompanyKeywordSolrListResponse(Long totalElements, Long page, Long pageSize, List<CompanyKeywordSolrResponse> content) {
        super(totalElements, page, pageSize);
        this.content = content;
    }

    public List<CompanyKeywordSolrResponse> getContent() {
        return content;
    }

    public void setContent(List<CompanyKeywordSolrResponse> content) {
        this.content = content;
    }
}
