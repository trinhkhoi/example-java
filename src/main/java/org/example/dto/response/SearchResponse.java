package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private List<CompanyKeywordSolrResponse> companies;
    private List<SearchUserDTO> users;
    private Object news;
    private Object posts;
}
