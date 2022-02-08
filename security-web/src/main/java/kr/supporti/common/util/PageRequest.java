package kr.supporti.common.util;

import java.util.ArrayList;
import java.util.List;

public class PageRequest {

    public static final String LIMIT_OFFSET_QUERY = " LIMIT #{pageRequest.limit} OFFSET #{pageRequest.offset} ";

    public static final String COUNT_START_QUERY = " SELECT COUNT(*) AS `total_rows` FROM ( ";

    public static final String COUNT_END_QUERY = " ) `count_query` ";

    public static final String SORT_START_QUERY = " SELECT * FROM ( ";

    public static final String SORT_END_QUERY = "" + " ) `sort_query` "
            + " <if test='pageRequest.sortList != null and !pageRequest.sortList.isEmpty()'> " + "     ORDER BY "
            + "         <foreach " + "             index='index' " + "             item='sort' "
            + "             collection='pageRequest.sortList' " + "             separator=',' " + "         > "
            + "             `sort_query`.`${sort.sortBy}` " + "             <choose> "
            + "                 <when test='sort.descending'> DESC </when> "
            + "                 <otherwise> ASC </otherwise> " + "             </choose> " + "         </foreach> "
            + " </if> ";

    private Integer page = 1;

    private Integer rowSize = 10;

    private Integer pageSize = 10;

    private Boolean isCount = true;

    private List<String> sort;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page == null) {
            page = 1;
        }
        this.page = (page < 1) ? 1 : page;
    }

    public Integer getRowSize() {
        return rowSize;
    }

    public void setRowSize(Integer rowSize) {
        if (rowSize == null) {
            rowSize = 10;
        }
        this.rowSize = (rowSize < 1) ? 10 : rowSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        this.pageSize = (pageSize < 1) ? 10 : pageSize;
    }

    public Integer getLimit() {
        return rowSize;
    }

    public Integer getOffset() {
        return (page - 1) * rowSize;
    }

    public Integer getRowStart() {
        return ((page - 1) * rowSize) + 1;
    }

    public Integer getRowEnd() {
        return ((page - 1) * rowSize) + rowSize;
    }

    public Boolean getIsCount() {
        return isCount;
    }

    public void setIsCount(Boolean isCount) {
        this.isCount = isCount;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public List<PageSort> getSortList() {
        List<PageSort> sortList = new ArrayList<>();
        if (sort != null) {
            for (int i = 0; i < sort.size(); i++) {
                String sortItem = sort.get(i);
                String[] items = sortItem.split(",");
                Boolean isNullSort = items.length >= 1
                        ? items[0].replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase().contains("!")
                        : false;
                String sortBy = items.length >= 1
                        ? items[0].replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase().replace("!", "")
                        : null;
                Boolean descending = items.length >= 2 && items[1].equalsIgnoreCase("DESC");
                PageSort pageSort = new PageSort();
                pageSort.setIsNullSort(isNullSort);
                pageSort.setSortBy(sortBy);
                pageSort.setDescending(descending);
                sortList.add(pageSort);
            }
        }
        return sortList;
    }

}

class PageSort {

    private Boolean isNullSort;

    private String sortBy;

    private Boolean descending;

    public Boolean getIsNullSort() {
        return isNullSort;
    }

    public void setIsNullSort(Boolean isNullSort) {
        this.isNullSort = isNullSort;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDescending() {
        return descending;
    }

    public void setDescending(Boolean descending) {
        this.descending = descending;
    }

}