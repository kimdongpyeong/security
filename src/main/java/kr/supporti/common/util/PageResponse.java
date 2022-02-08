package kr.supporti.common.util;

import java.util.List;

public class PageResponse<T> {

    private Integer page = 1;

    private Integer rowSize = 10;

    private Integer pageSize = 10;

    private Integer totalRows = 0;

    private Integer totalPages = 0;

    private Integer rowStart = 0;

    private Integer rowEnd = 0;

    private Integer pageStart = 0;

    private Integer pageEnd = 0;

    private Boolean isFirst = false;

    private Boolean isLast = false;

    private Boolean hasPrevious = false;

    private Boolean hasNext = false;

    private List<T> items = null;

    public PageResponse(Integer page, Integer rowSize, Integer pageSize, Integer totalRows) {
        if (page == null) {
            page = 1;
        }
        this.page = (page < 1) ? 1 : page;
        if (rowSize == null) {
            rowSize = 10;
        }
        this.rowSize = (rowSize < 1) ? 10 : rowSize;
        if (pageSize == null) {
            pageSize = 10;
        }
        this.pageSize = (pageSize < 1) ? 10 : pageSize;
        if (totalRows == null) {
            totalRows = 0;
        }
        this.totalRows = (totalRows < 0) ? 0 : totalRows;
        this.totalPages = (int) Math.ceil((double) this.totalRows / this.rowSize);
        this.page = (this.page > this.totalPages) ? this.totalPages : this.page;
        this.rowStart = 1 + (this.page * this.rowSize) - this.rowSize;
        this.rowEnd = (this.totalRows < (this.page * this.rowSize)) ? this.totalRows : (this.page * this.rowSize);
        this.pageStart = (((int) Math.ceil((double) this.page / this.pageSize) * this.pageSize) - this.pageSize) + 1;
        this.pageEnd = (this.totalPages < (this.pageStart + this.pageSize) - 1) ? this.totalPages
                : ((this.pageStart + this.pageSize) - 1);
        this.isFirst = (this.page == 1);
        this.isLast = (this.page.equals(this.totalPages));
        this.hasPrevious = this.pageStart > 1;
        this.hasNext = this.pageEnd < this.totalPages;
    }

    public PageResponse(PageRequest pageRequest, Integer totalRows) {
        this(pageRequest.getPage(), pageRequest.getRowSize(), pageRequest.getPageSize(), totalRows);
    }

    public Integer getPage() {
        return page;
    }

    public Integer getRowSize() {
        return rowSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getRowStart() {
        return rowStart;
    }

    public Integer getRowEnd() {
        return rowEnd;
    }

    public Integer getPageStart() {
        return pageStart;
    }

    public Integer getPageEnd() {
        return pageEnd;
    }

    public Boolean getIsFirst() {
        return isFirst;
    }

    public Boolean getIsLast() {
        return isLast;
    }

    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
