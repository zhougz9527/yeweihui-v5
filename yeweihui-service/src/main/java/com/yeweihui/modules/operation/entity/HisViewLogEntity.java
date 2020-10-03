package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yeweihui.modules.user.entity.UserEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史记录查看日志
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("his_view_log")
public class HisViewLogEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private Long zoneId;

	private Long uid;

	/** 模块类型 */
	private Integer type;

	private Long referId;

	private Date viewTime;

	private Integer recordStatus;

	/** 小区名称 */
	@TableField(exist = false)
	private String zoneName;

	/** 用户姓名 */
	@TableField(exist = false)
	private String realname;

	/** 用户实体 */
	@TableField(exist = false)
	private UserEntity userEntity;

	/** 记录实体 */
	@TableField(exist = false)
	private Object record;

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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getReferId() {
		return referId;
	}

	public void setReferId(Long referId) {
		this.referId = referId;
	}

	public Date getViewTime() {
		return viewTime;
	}

	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}

	public Object getRecord() {
		return record;
	}

	public void setRecord(Object record) {
		this.record = record;
	}
}
