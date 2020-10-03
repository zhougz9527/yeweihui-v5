package com.yeweihui.common.vo;

/**
 * Created by cutie on 2018/10/23.
 */
public class BasePageQueryParam {
    private String limit;
    private String page;
    private String sidx;
    private String order;
/*    private String _search;
    private String _;
    private String nd;*/

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /*public String get_search() {
        return _search;
    }

    public void set_search(String _search) {
        this._search = _search;
    }

    public String get_() {
        return _;
    }

    public void set_(String _) {
        this._ = _;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }*/
}
