package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.util.Date;

@TableName("notice_member")
public class NoticeMemberEntity {

    @TableId
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 通知id
     */
    private Long nid;
    /**
     * 通知接受人id
     */
    private Long uid;
    /**
     * 发起人id
     */
    private Long referUid;

    /**
     * 最后阅读时间
     */
    private Date lastReadTime;

    /**
     * 状态 1未读 2已读
     */
    private Integer status;

    @TableField(exist = false)
    private String memberRealname;

    @TableField(exist = false)
    private String memberAvatarUrl;

    @TableField(exist = false)
    private String memberRoleName;

    @TableField(exist = false)
    private Long memberZoneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getNid() {
        return nid;
    }

    public void setNid(Long nid) {
        this.nid = nid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getReferUid() {
        return referUid;
    }

    public void setReferUid(Long referUid) {
        this.referUid = referUid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Date lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public String getMemberRealname() {
        return memberRealname;
    }

    public void setMemberRealname(String memberRealname) {
        this.memberRealname = memberRealname;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public String getMemberRoleName() {
        return memberRoleName;
    }

    public void setMemberRoleName(String memberRoleName) {
        this.memberRoleName = memberRoleName;
    }

    public Long getMemberZoneId() {
        return memberZoneId;
    }

    public void setMemberZoneId(Long memberZoneId) {
        this.memberZoneId = memberZoneId;
    }
}
