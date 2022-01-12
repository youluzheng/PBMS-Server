package org.pbms.pbmsserver.common.response;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Data
public class PageData<T> implements Serializable {
    private List<T> list;
    private Long totalCount;
    private Integer currentPage;

    public PageData() {
        this.list = new ArrayList<>();
        this.totalCount = 0L;
    }

    /**
     * 构造分页返回信息
     * 建议统一使用PageData.getPageData()分页
     *
     * @param list
     * @param totalCount  总条数
     * @param currentPage 当前页
     */
    public PageData(List<T> list, Long totalCount, Integer currentPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
    }

    public PageData(List<T> list, Integer totalCount, Integer currentPage) {
        this.list = list;
        this.totalCount = (long) totalCount;
        this.currentPage = currentPage;
    }

    public PageData(List<T> list) {
        this.list = list;
        this.totalCount = (long) list.size();
    }

    public <S> PageData<S> convertType(List<S> list) {
        return new PageData<>(list, this.getTotalCount(), this.getCurrentPage());
    }

    /**
     * 分页查询
     * 例子：PageData.getPageData(req.getPageNo(), req.getPageSize(), () -> xxxMapper.selectByExample(example));
     *
     * @param pageNo   页数
     * @param pageSize 每页大小
     * @param supplier mapper方法的lambda表达式
     * @param <T>      mapper 放回对象
     * @return PageData
     */
    public static <T> PageData<T> getPageData(Integer pageNo, Integer pageSize, Supplier<List<T>> supplier) {
        if (pageNo != null && pageSize != null) {
            PageHelper.startPage(pageNo, pageSize);
        }
        List<T> list = supplier.get();
        PageHelper.clearPage();
        PageInfo<T> pageRes = new PageInfo<>(list);
        return new PageData<>(list, pageRes.getTotal(), pageRes.getPageNum());
    }


    public static <T, R> PageData<R> getPageData(Integer pageNo, Integer pageSize, Supplier<List<T>> supplier, Transfer<T, R> transfer) {
        if (pageNo != null && pageSize != null) {
            PageHelper.startPage(pageNo, pageSize);
        }
        List<T> list = supplier.get();
        PageHelper.clearPage();
        PageInfo<T> pageRes = new PageInfo<>(list);
        return new PageData<>((List<R>) list.stream().map(e -> transfer.to(e)).collect(Collectors.toList()), pageRes.getTotal(), pageRes.getPageNum());

    }
}
