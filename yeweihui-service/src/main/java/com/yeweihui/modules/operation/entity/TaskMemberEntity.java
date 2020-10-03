package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务人员表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("task_member")
public class TaskMemberEntity implements Serializable {
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
	 * 任务id
	 */
	private Long tid;
	/**
	 * 执行用户id
	 */
	private Long uid;
	/**
	 * 发任务用户id
	 */
	private Long referUid;
	/**
	 * 状态 1未完成 2已完成
	 */
	private Integer status;

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
	 * 设置：任务id
	 */
	public void setTid(Long tid) {
		this.tid = tid;
	}
	/**
	 * 获取：任务id
	 */
	public Long getTid() {
		return tid;
	}
	/**
	 * 设置：执行用户id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：执行用户id
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：发任务用户id
	 */
	public void setReferUid(Long referUid) {
		this.referUid = referUid;
	}
	/**
	 * 获取：发任务用户id
	 */
	public Long getReferUid() {
		return referUid;
	}
	/**
	 * 设置：状态 1未完成 2已完成
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 1未完成 2已完成
	 */
	public Integer getStatus() {
		return status;
	}
}
