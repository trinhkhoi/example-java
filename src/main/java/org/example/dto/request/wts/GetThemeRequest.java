package org.example.dto.request.wts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetThemeRequest {
    private String thsCode;
    private String thsType;
    private String nextKey;
    private int reqCount = 100;
}
