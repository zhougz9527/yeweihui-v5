package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作任务表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("task")
public class TaskEntity implements Serializable {
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
	 * 小区id
	 */
	private Long zoneId;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 总任务数
	 */
	private Integer total;
	/**
	 * 完成任务数
	 */
	private Integer checked;
	/**
	 * 状态 1完成2超时
	 */
	private Integer status;
	/**
	 * 截止日期
	 */
	private Date endDate;

	/**
	 * 记录状态 0删除 1业主不显示 2通过
	 */
	private Integer recordStatus;

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

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
	 * 设置：小区id
	 */
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	/**
	 * 获取：小区id
	 */
	public Long getZoneId() {
		return zoneId;
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
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：总任务数
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获取：总任务数
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置：完成任务数
	 */
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	/**
	 * 获取：完成任务数
	 */
	public Integer getChecked() {
		return checked;
	}
	/**
	 * 设置：状态 1完成2超时
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 1完成2超时
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：截止日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：截止日期
	 */
	public Date getEndDate() {
		return endDate;
	}
}
