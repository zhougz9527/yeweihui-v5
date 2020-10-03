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
 * 来往函件表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("paper")
public class PaperEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//element图片上传用这个
	@TableField(exist=false)
	private List<FileEntity> fileList;
	//文件类型
	@TableField(exist=false)
	private FileTypeEnum fileTypeEnum;

	//函件接收列表
	@TableField(exist=false)
	private List<PaperMemberEntity> paperMemberEntityList;

	//函件接收状态
	@TableField(exist=false)
	private Integer memberStatus;

	//函件接收状态中文
	@TableField(exist=false)
	private String memberStatusCn;

	//函件状态中文
	@TableField(exist=false)
	private String statusCn;

	//发函用户真实姓名
	@TableField(exist=false)
	private String realname;

	//发函用户头像
	@TableField(exist=false)
	private String avatarUrl;

	//小区名称
	@TableField(exist=false)
	private String zoneName;

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
	 * 发函人id
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
	 * 需要签收人数
	 */
	private Integer total;
	/**
	 * 已经签收人数
	 */
	private Integer checked;
	/**
	 * 状态 0未签收1已签收
	 */
	private Integer status;

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
	 * 设置：发函人id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：发函人id
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
	 * 设置：需要签收人数
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获取：需要签收人数
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置：已经签收人数
	 */
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	/**
	 * 获取：已经签收人数
	 */
	public Integer getChecked() {
		return checked;
	}
	/**
	 * 设置：状态 0未签收1已签收
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0未签收1已签收
	 */
	public Integer getStatus() {
		return status;
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

	public List<PaperMemberEntity> getPaperMemberEntityList() {
		return paperMemberEntityList;
	}

	public void setPaperMemberEntityList(List<PaperMemberEntity> paperMemberEntityList) {
		this.paperMemberEntityList = paperMemberEntityList;
	}


	public Integer getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Integer memberStatus) {
		this.memberStatus = memberStatus;
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

	public String getMemberStatusCn() {
		return memberStatusCn;
	}

	public void setMemberStatusCn(String memberStatusCn) {
		this.memberStatusCn = memberStatusCn;
	}

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
}
