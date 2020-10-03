package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("menu_map")
public class MenuMapEntity {

    @TableId
    private Long id;

    private String sysMenuName;

    private String type;

    private String maMenuName;

    private String maType;

    private String maMenuPage;

    private String firstLevel;

    private String secondLevel;

    private String moduleName;

    private boolean isHide;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String allowGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSysMenuName() {
        return sysMenuName;
    }

    public void setSysMenuName(String sysMenuName) {
        this.sysMenuName = sysMenuName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaMenuName() {
        return maMenuName;
    }

    public void setMaMenuName(String maMenuName) {
        this.maMenuName = maMenuName;
    }

    public String getMaType() {
        return maType;
    }

    public void setMaType(String maType) {
        this.maType = maType;
    }

    public String getMaMenuPage() {
        return maMenuPage;
    }

    public void setMaMenuPage(String maMenuPage) {
        this.maMenuPage = maMenuPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllowGroup() {
        return allowGroup;
    }

    public void setAllowGroup(String allowGroup) {
        this.allowGroup = allowGroup;
    }

    public String getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(String firstLevel) {
        this.firstLevel = firstLevel;
    }

    public String getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(String secondLevel) {
        this.secondLevel = secondLevel;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
