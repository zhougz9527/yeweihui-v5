package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 函件签收表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("paper_member")
public class PaperMemberEntity implements Serializable {
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
	 * 函件id
	 */
	private Long pid;
	/**
	 * 收函用户id
	 */
	private Long uid;
	/**
	 * 发函用户id
	 */
	private Long referUid;
	/**
	 * 是否签收
	 */
	private Integer status;

	@TableField(exist = false)
	private String realname;

	@TableField(exist = false)
	private String avatarUrl;


	@TableField(exist = false)
	private String statusCn;

	/**
	 * 签收时间
	 */
	private Date signTime;

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
	 * 设置：函件id
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * 获取：函件id
	 */
	public Long getPid() {
		return pid;
	}
	/**
	 * 设置：收函用户id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：收函用户id
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：发函用户id
	 */
	public void setReferUid(Long referUid) {
		this.referUid = referUid;
	}
	/**
	 * 获取：发函用户id
	 */
	public Long getReferUid() {
		return referUid;
	}
	/**
	 * 设置：是否签收
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：是否签收
	 */
	public Integer getStatus() {
		return status;
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

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}
}
