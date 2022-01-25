package org.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.common.jpa.SearchInput;

@Builder
@AllArgsConstructor
public class CompanySolrRequest extends SearchInput {
    private String name ;
    private String searchType;

    public CompanySolrRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchType() {
        return searchType != null ? searchType.toUpperCase() : "COMPANY";
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
