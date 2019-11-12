package com.zss.myspringboot.util;

import java.util.ArrayList;
import java.util.Collection;

public class PaginationSupport<T> {
    private int pageNumber = 1;
    private int pageSize = 15;
    private int total = 0;
    private Collection<T> paginationResults = new ArrayList();

    public PaginationSupport() {
    }

    public PaginationSupport(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPages() {
        if (this.total == 0) {
            return 0;
        } else {
            int pages = this.total / this.pageSize;
            if (this.total % this.pageSize != 0) {
                ++pages;
            }

            return pages;
        }
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getTotal() {
        return this.total;
    }

    public Collection<T> getPaginationResults() {
        return this.paginationResults;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPaginationResults(Collection<T> paginationResults) {
        this.paginationResults = paginationResults;
    }

    public boolean getSuccess() {
        return true;
    }
}

