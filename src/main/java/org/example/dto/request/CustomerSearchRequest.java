package org.example.dto.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.example.util.Constant;

@Data
public class CustomerSearchRequest implements ValidRequest {
    private String keyword;
    private Constant.SearchKeywordType searchType = Constant.SearchKeywordType.ALL;

    public Constant.SearchKeywordType getSearchType() {
        return searchType != null ? searchType : Constant.SearchKeywordType.ALL;
    }

    public String getKeyword() {
        return keyword != null ? keyword.trim() : null;
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNotBlank(keyword);
    }
}
