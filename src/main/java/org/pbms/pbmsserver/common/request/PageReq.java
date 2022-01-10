package org.pbms.pbmsserver.common.request;

/**
 * @author wangjun
 * @date 2022/1/10 13:40
 */
public class PageReq {
    private int pageSize = 10;
    private int pageNo = 1;

    public PageReq() {
    }

    public PageReq(int pageSize, int pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }
}
