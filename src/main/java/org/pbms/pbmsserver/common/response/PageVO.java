package org.pbms.pbmsserver.common.response;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;
import java.util.function.Supplier;

@Data
public class PageVO<T> {
    // 当前页码
    private int pageId;
    // 页尺寸
    private int pageSize;
    // 总页数
    private long pageCount;
    // 总条数
    private long totalCount;
    // 当页数据
    private List<T> data;

    public PageVO(int pageId, int pageSize, Supplier<List<T>> supplier) {
        if (pageId <= 0) {
            pageId = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        PageInfo<T> pageInfo = PageHelper.startPage(pageId, pageSize).doSelectPageInfo(supplier::get);
        this.pageId = pageId;
        this.pageSize = pageSize;
        this.totalCount = pageInfo.getTotal();
        this.pageCount = pageInfo.getPages();
        this.data = pageInfo.getList();
        PageHelper.clearPage();
    }

    public PageVO(int pageId, int pageSize, long totalCount, List<T> data) {
        if (pageId <= 0) {
            pageId = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        this.pageId = pageId;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
        this.pageCount = this.totalCount / this.pageSize + (this.totalCount % this.pageSize == 0 ? 0 : 1);
    }
}
