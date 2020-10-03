package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.util.Date;
import java.util.List;
import java.util.Set;

@TableName("notice")
public class NoticeEntity {

    /**
     * 发送小区ID Set，至少有一个值
     */
    @TableField(exist = false)
    private Set<Long> zoneIdSet;

    @TableField(exist = false)
    private String zoneNameListStr;

    private String noticeZoneIdTag;

    /**
     * 发送对象
     */
    @TableField(exist = false)
    private List<NoticeMemberEntity> noticeMemberEntityList;

    /**
     * 通知发起用户真实姓名
     */
    @TableField(exist = false)
    private String realname;
    /**
     * 通知发起用户头像
     */
    @TableField(exist = false)
    private String avatarUrl;
    /**
     * 小区名称
     */
    @TableField(exist = false)
    private String zoneName;

    /**
     * 观察者id
     */
    @TableField(exist = false)
    private Long viewUid;

    /**
     * 阅读者状态 1未读2已读
     */
    @TableField(exist = false)
    private Integer memberStatus;

    /**
     * 创建人的组别
     */
    @TableField(exist = false)
    private String creatorGroup;

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
     * 标题
     */
    private String title;
    /**
     * 小区id
     */
    private Long zoneId;

    /**
     * 发起人ID
     */
    private Long uid;

    /**
     * 通知内容html
     */
    String content;

    /**
     * 已阅读人数
     */
    private Integer readCount;

    /**
     * 状态 1未发布 2发布
     */
    private Integer status;

    private Integer recordStatus;

    public String getZoneNameListStr() {
        return zoneNameListStr;
    }

    public void setZoneNameListStr(String zoneNameListStr) {
        this.zoneNameListStr = zoneNameListStr;
    }

    public Set<Long> getZoneIdSet() {
        return zoneIdSet;
    }

    public void setZoneIdSet(Set<Long> zoneIdSet) {
        this.zoneIdSet = zoneIdSet;
    }

    public String getNoticeZoneIdTag() {
        return noticeZoneIdTag;
    }

    public void setNoticeZoneIdTag(String noticeZoneIdTag) {
        this.noticeZoneIdTag = noticeZoneIdTag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<NoticeMemberEntity> getNoticeMemberEntityList() {
        return noticeMemberEntityList;
    }

    public void setNoticeMemberEntityList(List<NoticeMemberEntity> noticeMemberEntityList) {
        this.noticeMemberEntityList = noticeMemberEntityList;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(Integer recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Long getViewUid() {
        return viewUid;
    }

    public void setViewUid(Long viewUid) {
        this.viewUid = viewUid;
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getCreatorGroup() {
        return creatorGroup;
    }

    public void setCreatorGroup(String creatorGroup) {
        this.creatorGroup = creatorGroup;
    }
}
