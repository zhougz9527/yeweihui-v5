package com.yeweihui.modules.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-15 15:09:44
 */
@TableName("multimedia")
public class MultimediaEntity implements Serializable {
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
	 * 名称
	 */
	private String name;
	/**
	 * url
	 */
	private String url;
	/**
	 * 文件大小
	 */
	private Integer size;
	/**
	 * 文件描述
	 */
	private String description;
	/**
	 * 关联id
	 */
	private Long relatedId;
	/**
	 * 图片对应业务类型 1报销 2会议 3发函 4用章 5任务 6投票 7会议日志 8通知 9公示
	 */
	private Integer relatedType;
	/**
	 * 文件类型
	 */
	private String fileType;

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
	 * 设置：名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：文件大小
	 */
	public void setSize(Integer size) {
		this.size = size;
	}
	/**
	 * 获取：文件大小
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * 设置：文件描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：文件描述
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：关联id
	 */
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	/**
	 * 获取：关联id
	 */
	public Long getRelatedId() {
		return relatedId;
	}
	/**
	 * 设置：图片对应业务类型 1报销 2会议 3发函 4用章 5任务 6投票
	 */
	public void setRelatedType(Integer relatedType) {
		this.relatedType = relatedType;
	}
	/**
	 * 获取：图片对应业务类型 1报销 2会议 3发函 4用章 5任务 6投票
	 */
	public Integer getRelatedType() {
		return relatedType;
	}
	/**
	 * 设置：文件类型
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/**
	 * 获取：文件类型
	 */
	public String getFileType() {
		return fileType;
	}

	public MultimediaEntity() {
	}

	public MultimediaEntity(Date createTime, Date updateTime, String name, String url, Integer size, String description, Long relatedId, Integer relatedType, String fileType) {
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.name = name;
		this.url = url;
		this.size = size;
		this.description = description;
		this.relatedId = relatedId;
		this.relatedType = relatedType;
		this.fileType = fileType;
	}
}
