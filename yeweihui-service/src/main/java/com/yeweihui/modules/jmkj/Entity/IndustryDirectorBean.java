package com.yeweihui.modules.jmkj.Entity;

import io.swagger.annotations.ApiModelProperty;

public class IndustryDirectorBean {

    @ApiModelProperty("行业主管id")
    private Long id;

    @ApiModelProperty("主管名字")
    private String realname;

    @ApiModelProperty("行政级别")
    private String level;

    @ApiModelProperty("对应行政级别的行政区域表的id")
    private Long divisionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
    }
}
