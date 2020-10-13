package com.yeweihui.modules.jmkj.Entity;

import io.swagger.annotations.ApiModelProperty;

public class JmkjLoginStatusBean {

    private Long id;

    @ApiModelProperty("用户id")
    private Long uid;

    @ApiModelProperty("在线时长")
    private Long onLineTime;

    @ApiModelProperty("登录次数")
    private Long loginTimes;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("修改时间")
    private Long updateTime;

    @ApiModelProperty("当前月份")
    private Long monthTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getOnLineTime() {
        return onLineTime;
    }

    public void setOnLineTime(Long onLineTime) {
        this.onLineTime = onLineTime;
    }

    public Long getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Long loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getMonthTime() {
        return monthTime;
    }

    public void setMonthTime(Long monthTime) {
        this.monthTime = monthTime;
    }
}
