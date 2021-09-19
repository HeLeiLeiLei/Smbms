package com.hl.util;

import java.util.List;

public class PageUtils {
    private int page;//当前页

    private int pagesize;//页面数据条数

    private int indexpage=1;//首页

    private int endpage;//尾页

    private int count;//总数据条数

    private int pageNumber;//总页面数

    private List<Object> list;//得到的数据放入list集合中

    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getPagesize() {
        return pagesize;
    }
    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }
    public int getIndexpage() {
        return indexpage;
    }
    public void setIndexpage(int indexpage) {
        this.indexpage = indexpage;
    }
    public int getEndpage() {

        return endpage;
    }
    public void setEndpage() {
        this.endpage=pageNumber;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getPagenumber() {
        return pageNumber;
    }
    public void setPagenumber() {
        this.pageNumber=(count%pagesize==0)?count/pagesize:count/pagesize+1;
    }
    public List<Object> getList() {
        return list;
    }
    public void setList(List<Object> list) {
        this.list = list;
    }
    @Override
    public String toString() {
        return "PageBean [page=" + page + ", pagesize=" + pagesize + ", indexpage=" + indexpage + ", endpage="
                + endpage + ", count=" + count + ", pagenumber=" + pageNumber + ", list=" + list + "]";
    }
}