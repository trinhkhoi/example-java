package org.example.dto.response.parent;

/**
 * @author nguyentienthanh
 * Date: 2019-08-26 10:07
 * Description:
 */
public class SearchResult {
    private Long totalElements = 0l;
    private Long totalPages    = 0l;
    private Long page;
    private Long pageSize;

    public SearchResult() {
    }

    public SearchResult(Long totalElements, Long page, Long pageSize) {
        this.totalElements = totalElements;
        this.page = page;
        this.pageSize = pageSize > 0 ? pageSize : 1L;
        long temp = this.totalElements / this.pageSize;
        this.totalPages = this.totalElements % this.pageSize == 0 ? temp : temp + 1;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

}
