package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 表决成员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("vote_member")
public class VoteMemberEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//投票人员真实姓名
	@TableField(exist = false)
	private String memberRealname;

	//投票人员真实姓名
	@TableField(exist = false)
	private String memberAvatarUrl;

	//投票参与状态中文
	@TableField(exist = false)
	private String statusCn;

	@TableField(exist = false)
	private String memberRoleName;

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
	 * 表决id
	 */
	private Long vid;
	/**
	 * 参与表决用户id
	 */
	private Long uid;
	/**
	 * 发起人id
	 */
	private Long referUid;
	/**
	 * 类型 1表决 2抄送
	 */
	private Integer type;
	/**
	 * 状态 1同意 2反对 3弃权 4超时
	 */
	private Integer status;
	/**
	 * 支持选项1
	 */
	private Integer item1;
	/**
	 * 支持选项2
	 */
	private Integer item2;
	/**
	 * 支持选项3
	 */
	private Integer item3;
	/**
	 * 都不同意
	 */
	private Integer item4;
	/**
	 * 备注
	 */
	private String remark;

	/** 签名url */
	private String verifyUrl;
	/**
	 * 表决时间
	 */
	private Date voteTime;

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
	 * 设置：表决id
	 */
	public void setVid(Long vid) {
		this.vid = vid;
	}
	/**
	 * 获取：表决id
	 */
	public Long getVid() {
		return vid;
	}
	/**
	 * 设置：参与表决用户id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：参与表决用户id
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：发起人id
	 */
	public void setReferUid(Long referUid) {
		this.referUid = referUid;
	}
	/**
	 * 获取：发起人id
	 */
	public Long getReferUid() {
		return referUid;
	}
	/**
	 * 设置：类型 1表决 2抄送
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型 1表决 2抄送
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：状态 1同意 2反对 3弃权 4超时
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 1同意 2反对 3弃权 4超时
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：支持选项1
	 */
	public void setItem1(Integer item1) {
		this.item1 = item1;
	}
	/**
	 * 获取：支持选项1
	 */
	public Integer getItem1() {
		return item1;
	}
	/**
	 * 设置：支持选项2
	 */
	public void setItem2(Integer item2) {
		this.item2 = item2;
	}
	/**
	 * 获取：支持选项2
	 */
	public Integer getItem2() {
		return item2;
	}
	/**
	 * 设置：支持选项3
	 */
	public void setItem3(Integer item3) {
		this.item3 = item3;
	}
	/**
	 * 获取：支持选项3
	 */
	public Integer getItem3() {
		return item3;
	}
	/**
	 * 设置：都不同意
	 */
	public void setItem4(Integer item4) {
		this.item4 = item4;
	}
	/**
	 * 获取：都不同意
	 */
	public Integer getItem4() {
		return item4;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}

	/**
	 * 设置：表决时间
	 */
	public void setVoteTime(Date voteTime) {
		this.voteTime = voteTime;
	}
	/**
	 * 获取：表决时间
	 */
	public Date getVoteTime() {
		return voteTime;
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
