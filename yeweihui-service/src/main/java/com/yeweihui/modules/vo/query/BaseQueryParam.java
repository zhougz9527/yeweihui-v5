package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BaseQueryParam {
    @ApiModelProperty(value = "每页条数（默认10）")
    private String limit = "10";

    @ApiModelProperty(value = "页码（默认1）")
    private String page = "1";

    @ApiModelProperty(value = "排序字段")
    private String sidx;

    @ApiModelProperty(value = "排序顺序（asc,desc）")
    private String order = "asc";

    @ApiModelProperty(value = "授权查看的小区id")
    private Long authZoneId;

    @ApiModelProperty(value = "行业主管授权查看的所有小区id")
    private List<Long> authZoneIdList;

    @ApiModelProperty(value = "可显示的权限等级")
    private Integer minRecordStatus;

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

    public Long getAuthZoneId() {
        return authZoneId;
    }

    public void setAuthZoneId(Long authZoneId) {
        this.authZoneId = authZoneId;
    }

    public List<Long> getAuthZoneIdList() {
        return authZoneIdList;
    }

    public void setAuthZoneIdList(List<Long> authZoneIdList) {
        this.authZoneIdList = authZoneIdList;
    }

    public Integer getMinRecordStatus() {
        return minRecordStatus;
    }

    public void setMinRecordStatus(Integer minRecordStatus) {
        this.minRecordStatus = minRecordStatus;
    }
}
