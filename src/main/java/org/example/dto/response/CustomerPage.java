package org.example.dto.response;

import com.google.api.client.util.Lists;
import org.example.dto.response.parent.SearchResult;

import java.util.List;

/**
 * Customer page
 * @author khoitd
 */

public class CustomerPage extends SearchResult {

    private List<CustomerDto> data = Lists.newArrayList();

    public CustomerPage() {
    }

    public CustomerPage(Long totalElements, Long page, Long pageSize, List<CustomerDto> data) {
        super(totalElements, page, pageSize);
        this.data = data;
    }

    public List<CustomerDto> getData() {
        return data;
    }

    public void setContent(List<CustomerDto> data) {
        this.data = data;
    }
}
