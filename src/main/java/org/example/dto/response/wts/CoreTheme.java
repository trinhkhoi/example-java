package org.example.dto.response.wts;

import lombok.Data;
import org.common.utils.DataUtil;

import java.util.List;

@Data
public class CoreTheme {
    private List<OriginTheme> list;
    private Boolean continued;
    private int totalCount;
    private String nextKey;

    public String getNextKey() {
        return DataUtil.safeToString(nextKey);
    }
}
