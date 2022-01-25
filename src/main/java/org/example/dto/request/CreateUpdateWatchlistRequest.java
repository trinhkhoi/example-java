package org.example.dto.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CreateUpdateWatchlistRequest {
    private String name;
    private Boolean share;
    private String stocks;

    public Boolean getShare() {
        return share != null ? share : false;    // auto not share as default
    }

    public String getName() {
        return StringUtils.isBlank(name) ? "My watchlist" : name;
    }

}
