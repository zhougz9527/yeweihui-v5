package com.yeweihui.modules.user.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 小区菜单权限关联表
 */
@TableName("zone_menu_map")
public class ZoneMenuMapEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 小区id
     */
    private Long zoneId;

    /**
     * 组id
     */
    private Long groupId;

    /**
     * 菜单权限id集合
     */
    private String menuMapIds;

    @TableField(exist = false)
    private String groupName;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public ZoneMenuMapEntity() {
    }

    public ZoneMenuMapEntity(Long zoneId, Long groupId, String menuMapIds, Date createTime, Date updateTime) {
        this.zoneId = zoneId;
        this.groupId = groupId;
        this.menuMapIds = menuMapIds;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getMenuMapIds() {
        return menuMapIds;
    }

    public void setMenuMapIds(String menuMapIds) {
        this.menuMapIds = menuMapIds;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
