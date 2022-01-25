package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistResponse {
    private Long customerId;
    private String name;
    private Boolean share;
    private List<CompanyResponse> stocks;
}
