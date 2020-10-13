package com.yeweihui.modules.jmkj.Entity;

import io.swagger.annotations.ApiModelProperty;

public class PerformanceRateBean {

    @ApiModelProperty("参与用户id")
    private Long uid;

    @ApiModelProperty("用户名字")
    private String realname;

    @ApiModelProperty("用户头像")
    private String avatarUrl;

    @ApiModelProperty("用户履职率")
    private Float proportion;

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

    public Float getProportion() {
        return proportion;
    }

    public void setProportion(Float proportion) {
        this.proportion = proportion;
    }
}
