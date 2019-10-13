package org.javatribe.lottery.entity;

import lombok.Data;

import java.util.List;

/**
 * 分页对象
 *
 * @author JimZiSing
 */
@Data
public class Page<T> {
    /**
     * 总记录数
     */
    private int totalRecord;
    /**
     * 一页数据条数,默认8条
     */
    private int pageSize = 8;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 当前页
     */
    private int pageNumber;
    /**
     * 分页数据
     */
    private List<T> pages;

    public Page() {
    }

    public Page(int pageNumber, int pageSize) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public Page(int totalRecord, int pageSize, int totalPage, int pageNumber) {
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
    }

    public Page(int totalRecord, int pageSize, int totalPage, int pageNumber, List<T> pages) {
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.pageNumber = pageNumber;
        this.pages = pages;
    }
}
