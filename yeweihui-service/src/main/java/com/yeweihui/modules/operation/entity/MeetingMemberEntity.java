package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 参会人员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("meeting_member")
public class MeetingMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
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
	 * 会议id
	 */
	private Long mid;
	/**
	 * 参加会议的人
	 */
	private Long uid;
	/**
	 * 会议发起人
	 */
	private Long referUid;
	/**
	 * 类型 1参会 2抄送
	 */
	private Integer type;
	/**
	 * 签到时间
	 */
	private Date signTime;
	/**
	 * 签字时间
	 */
	private Date signNameTime;
	/**
	 * 状态 0待开会 1已签到 2已签字
	 */
	private Integer status;

	/**
	 * 签到url
	 */
	private String signInUrl;

	/**
	 * 签字url
	 */
	private String signEndUrl;

	/**
	 * 参会人员真实姓名
	 */
	@TableField(exist = false)
	private String memberRealname;

	/**
	 * 参会状态中文
	 */
	@TableField(exist = false)
	private String statusCn;

	/**
	 * 参会人员头像
	 */
	@TableField(exist = false)
	private String avatarUrl;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：会议id
	 */
	public void setMid(Long mid) {
		this.mid = mid;
	}
	/**
	 * 获取：会议id
	 */
	public Long getMid() {
		return mid;
	}
	/**
	 * 设置：参加会议的人
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：参加会议的人
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：会议发起人
	 */
	public void setReferUid(Long referUid) {
		this.referUid = referUid;
	}
	/**
	 * 获取：会议发起人
	 */
	public Long getReferUid() {
		return referUid;
	}
	/**
	 * 设置：类型 1参会 2抄送
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型 1参会 2抄送
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：签到时间
	 */
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	/**
	 * 获取：签到时间
	 */
	public Date getSignTime() {
		return signTime;
	}
	/**
	 * 设置：签字时间
	 */
	public void setSignNameTime(Date signNameTime) {
		this.signNameTime = signNameTime;
	}
	/**
	 * 获取：签字时间
	 */
	public Date getSignNameTime() {
		return signNameTime;
	}
	/**
	 * 设置：状态 1已签到 2已签字
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 1已签到 2已签字
	 */
	public Integer getStatus() {
		return status;
	}

	public String getMemberRealname() {
		return memberRealname;
	}

	public void setMemberRealname(String memberRealname) {
		this.memberRealname = memberRealname;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public String getSignInUrl() {
		return signInUrl;
	}

	public void setSignInUrl(String signInUrl) {
		this.signInUrl = signInUrl;
	}

	public String getSignEndUrl() {
		return signEndUrl;
	}

	public void setSignEndUrl(String signEndUrl) {
		this.signEndUrl = signEndUrl;
	}
}
