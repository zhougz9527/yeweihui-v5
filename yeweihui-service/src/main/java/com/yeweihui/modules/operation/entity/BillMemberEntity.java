package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 报销审批表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("bill_member")
public class BillMemberEntity implements Serializable {
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
	 * 报销id
	 */
	private Long bid;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 参与用户id
	 */
	private Long referUid;
	/**
	 * 类型 1审批 2抄送
	 */
	private Integer type;
	/**
	 * 状态 0未审批 1已通过 2未通过
	 */
	private Integer status;

	//审批人
	@TableField(exist = false)
	private String memberRealname;


	//状态 0未审批 1已通过 2未通过
	@TableField(exist = false)
	private String statusCn;

	//头像url
	@TableField(exist = false)
	private String avatarUrl;

	/**
	 * 审批时间
	 */
	private Date verifyTime;


	private String verifyUrl;

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
	 * 设置：报销id
	 */
	public void setBid(Long bid) {
		this.bid = bid;
	}
	/**
	 * 获取：报销id
	 */
	public Long getBid() {
		return bid;
	}
	/**
	 * 设置：用户id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：用户id
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：参与用户id
	 */
	public void setReferUid(Long referUid) {
		this.referUid = referUid;
	}
	/**
	 * 获取：参与用户id
	 */
	public Long getReferUid() {
		return referUid;
	}
	/**
	 * 设置：状态 0未审批 1已通过 2未通过
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0未审批 1已通过 2未通过
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

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
