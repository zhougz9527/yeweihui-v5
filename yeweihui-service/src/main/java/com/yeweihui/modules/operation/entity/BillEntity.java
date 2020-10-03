package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.vo.admin.file.FileEntity;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报销表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("bill")
public class BillEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//element图片上传用这个
	@TableField(exist=false)
	private List<FileEntity> fileList;
	//文件类型
	@TableField(exist=false)
	private FileTypeEnum fileTypeEnum;

	//报销审批列表
	@TableField(exist=false)
	private List<BillMemberEntity> verifyMemberEntityList;

	//抄送人员
	@TableField(exist=false)
	private List<BillMemberEntity> copyToMemberEntityList;

	//报销用户名称
	@TableField(exist = false)
	private String realname;

	//打印日期
	@TableField(exist = false)
	private Date printDate;

	//打印日期
	@TableField(exist = false)
	private String avatarUrl;

	//审批参与状态
	@TableField(exist = false)
	private Integer memberStatus;

	//审批参与状态中文
	@TableField(exist = false)
	private String memberStatusCn;

	//小区名称
	@TableField(exist = false)
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
	 * 用户id
	 */
	private Long uid;
	/**
	 * 报销人
	 */
	private String name;
	/**
	 * 报销金额
	 */
	private BigDecimal money;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 发生日期
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date happenDate;
	/**
	 * 报销类型 1小额2大额
	 */
	private Integer type;
	/**
	 * 状态 0等待 1通过 2未通过
	 */
	private Integer status;
	/**
	 * 需要审批人数
	 */
	private Integer total;
	/**
	 * 已经审核人数
	 */
	private Integer checked;

	/**
	 * 状态 0等待 1通过 2未通过
	 */
	@TableField(exist = false)
	private String statusCn;
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
	 * 设置：报销人
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：报销人
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：报销金额
	 */
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	/**
	 * 获取：报销金额
	 */
	public BigDecimal getMoney() {
		return money;
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
	 * 设置：发生日期
	 */
	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}
	/**
	 * 获取：发生日期
	 */
	public Date getHappenDate() {
		return happenDate;
	}
	/**
	 * 设置：报销类型 1小额2大额
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：报销类型 1小额2大额
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：状态 0等待1通过2未通过
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0等待1通过2未通过
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：需要审批人数
	 */
	public void setTotal(Integer total) {
		this.total = total;
	}
	/**
	 * 获取：需要审批人数
	 */
	public Integer getTotal() {
		return total;
	}
	/**
	 * 设置：已经审核人数
	 */
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	/**
	 * 获取：已经审核人数
	 */
	public Integer getChecked() {
		return checked;
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

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public List<BillMemberEntity> getVerifyMemberEntityList() {
		return verifyMemberEntityList;
	}

	public void setVerifyMemberEntityList(List<BillMemberEntity> verifyMemberEntityList) {
		this.verifyMemberEntityList = verifyMemberEntityList;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Integer getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Integer memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getMemberStatusCn() {
		return memberStatusCn;
	}

	public void setMemberStatusCn(String memberStatusCn) {
		this.memberStatusCn = memberStatusCn;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public List<BillMemberEntity> getCopyToMemberEntityList() {
		return copyToMemberEntityList;
	}

	public void setCopyToMemberEntityList(List<BillMemberEntity> copyToMemberEntityList) {
		this.copyToMemberEntityList = copyToMemberEntityList;
	}
}
