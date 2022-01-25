package org.example.dto.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.common.jpa.SearchInput;
import org.common.utils.JSON;

import javax.validation.constraints.NotNull;

@Data
public class SearchCustomerFollowRequest extends SearchInput {

    @NotNull
    private Long idCustomer;
    private String fullName;

    public String getFullName() {
        return StringUtils.isNotBlank(fullName) ? fullName.trim() : StringUtils.EMPTY;
    }

    @Override
    public String toString() {
        try {
            return JSON.toPrettyJson(this);
        } catch (com.fasterxml.jackson.core.JsonProcessingException jpe) {
            LoggerFactory.getLogger(SearchCustomerFollowRequest.class).error("error with toString", jpe);
            return super.toString();
        }
    }
}
