package com.hl.util;

public class Page {

    int start;// 开始数据的索引
    int currentPage;//当前页
    int pageSize;// 每页显示的数据量
    int indexPage;//首页
    int lastPage;//尾页
    int pageNum;//页面的数量
    int totalNum;//总条数

    public Page(int currentPage, int pageSize,int totalNum) {
        super();
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalNum=totalNum;
        this.start=(currentPage-1)*pageSize;
        this.indexPage=1;
        if(totalNum%pageSize==0){
            this.pageNum=totalNum/pageSize;
        }else {
            this.pageNum=totalNum/pageSize+1;
        }
        this.lastPage=pageNum;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        if(start<=0){
            this.start=0;
        }else if(start >= pageNum){
            this.start=(pageNum-1)*pageSize;
        }else {
            this.start = (start-1)*pageSize;
        }

    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage <= 0){
            this.currentPage = 1;
        }else if(currentPage >= pageNum){
            this.currentPage = pageNum;
        }else {
            this.currentPage=currentPage;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(int indexPage) {
        this.indexPage = indexPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}