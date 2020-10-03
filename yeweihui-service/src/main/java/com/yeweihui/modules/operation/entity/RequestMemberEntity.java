package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 审批成员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("request_member")
public class RequestMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private String memberRoleName;

	@TableField(exist = false)
	private String realname;

	@TableField(exist = false)
	private String avatarUrl;

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
	 * 审批id
	 */
	private Long rid;
	/**
	 * 审批uid
	 */
	private Long uid;
	/**
	 * 发起人uid
	 */
	private Long referUid;
	/**
	 * 类型 1审批 2抄送
	 */
	private Integer type;
	/**
	 * 第几个审批
	 */
	private Integer step;
	/**
	 * 状态 0未审核 1通过 2未通过
	 */
	private Integer status;
	/**
	 * 备注
	 */
	private String notice;
	/**
	 * 审核时间
	 */
	private Date verifyTime;

	/** 签名url */
	private String verifyUrl;

	//中文显示
	/**
	 * 状态 0未审核 1通过 2未通过
	 */
	@TableField(exist = false)
	private String statusCn;

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
	 * 设置：审批id
	 */
	public void setRid(Long rid) {
		this.rid = rid;
	}
	/**
	 * 获取：审批id
	 */
	public Long getRid() {
		return rid;
	}
	/**
	 * 设置：审批uid
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：审批uid
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：发起人uid
	 */
	public void setReferUid(Long referUid) {
		this.referUid = referUid;
	}
	/**
	 * 获取：发起人uid
	 */
	public Long getReferUid() {
		return referUid;
	}
	/**
	 * 设置：类型 1审批 2抄送
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型 1审批 2抄送
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：第几个审批
	 */
	public void setStep(Integer step) {
		this.step = step;
	}
	/**
	 * 获取：第几个审批
	 */
	public Integer getStep() {
		return step;
	}
	/**
	 * 设置：状态 0未审核 1通过 2未通过
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0未审核 1通过 2未通过
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：备注
	 */
	public void setNotice(String notice) {
		this.notice = notice;
	}
	/**
	 * 获取：备注
	 */
	public String getNotice() {
		return notice;
	}
	/**
	 * 设置：审核时间
	 */
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getVerifyTime() {
		return verifyTime;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
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

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public String getMemberRoleName() {
		return memberRoleName;
	}

	public void setMemberRoleName(String memberRoleName) {
		this.memberRoleName = memberRoleName;
	}
}
