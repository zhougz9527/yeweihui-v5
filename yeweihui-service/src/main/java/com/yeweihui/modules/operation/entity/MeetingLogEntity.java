package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.vo.admin.file.FileEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会议记录表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("meeting_log")
public class MeetingLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//element图片上传用这个
	@TableField(exist=false)
	private List<FileEntity> fileList;
	//文件类型
	@TableField(exist=false)
	private FileTypeEnum fileTypeEnum;

	//日志用户头像
	@TableField(exist=false)
	private String meetingLogMemberAvatarUrl;

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
	 * 发言人
	 */
	private Long uid;
	/**
	 * 内容
	 */
	private String content;

	@TableField(exist = false)
	private String memberRealname;

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
	 * 设置：发言人
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：发言人
	 */
	public Long getUid() {
		return uid;
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

	public String getMemberRealname() {
		return memberRealname;
	}

	public void setMemberRealname(String memberRealname) {
		this.memberRealname = memberRealname;
	}

	public List<FileEntity> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileEntity> fileList) {
		this.fileList = fileList;
	}

	public FileTypeEnum getFileTypeEnum() {
		return fileTypeEnum;
	}

	public void setFileTypeEnum(FileTypeEnum fileTypeEnum) {
		this.fileTypeEnum = fileTypeEnum;
	}

	public String getMeetingLogMemberAvatarUrl() {
		return meetingLogMemberAvatarUrl;
	}

	public void setMeetingLogMemberAvatarUrl(String meetingLogMemberAvatarUrl) {
		this.meetingLogMemberAvatarUrl = meetingLogMemberAvatarUrl;
	}
}
