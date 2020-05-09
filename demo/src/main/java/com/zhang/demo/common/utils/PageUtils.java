package com.zhang.demo.common.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageSerializable;

import java.util.Collection;
import java.util.List;

/**
 * 封装分页工具类（对mybatis分页插件返回值进行整理）
 * @author zhang
 * @date 2020-05-09 14:15:50
 * @param <T>
 */
public class PageUtils<T> extends PageSerializable<T> {
    /**
     * 页数
     */
    private int pageNum;
    /**
     * 条数
     */
    private int pageSize;
    /**
     * 总页数
     */
    private int totalPage;

    public PageUtils(List<T> list) {
        this(list, 8);
    }

    public PageUtils(List<T> list, int navigatePages) {
        super(list);
        if (list instanceof Page) {
            Page page = (Page)list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.totalPage = page.size();
        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();
            this.totalPage = list.size();
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
