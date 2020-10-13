package com.yeweihui.modules.jmkj.Entity;

import io.swagger.annotations.ApiModelProperty;

public class PerformanceOfDutiesBean {

    @ApiModelProperty("参与用户id")
    private Long uid;

    @ApiModelProperty("参与用户名字")
    private String realname;

    @ApiModelProperty("参与用户头像")
    private String avatarUrl;

    @ApiModelProperty("数量")
    private Long num;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
