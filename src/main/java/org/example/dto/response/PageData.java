package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties({"pageable", "sort"})
public class PageData<T> extends PageImpl<T> {
    public PageData(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageData(List<T> content) {
        super(content);
    }
}
