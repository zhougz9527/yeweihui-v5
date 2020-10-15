package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeweihui.modules.enums.FileTypeEnum;
import com.yeweihui.modules.vo.admin.file.FileEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 事务表决表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("vote")
public class VoteEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//element图片上传用这个
	@TableField(exist=false)
	private List<FileEntity> fileList;
	//文件类型
	@TableField(exist=false)
	private FileTypeEnum fileTypeEnum;
	//投票人员
	@TableField(exist=false)
	private List<VoteMemberEntity> voteMemberEntityList;
	//抄送人员
	@TableField(exist=false)
	private List<VoteMemberEntity> copy2VoteMemberEntityList;
	//参与状态
	@TableField(exist=false)
	private Integer memberStatus;
	//参与状态
	@TableField(exist=false)
	private String memberStatusCn;
	//发起用户真实姓名
	@TableField(exist=false)
	private String realname;
	//发起用户头像
	@TableField(exist=false)
	private String avatarUrl;

	/**
	 * 投票类型 1实名 2匿名
	 */
	@TableField(exist=false)
	private String typeCn;
	@TableField(exist=false)
	private int item1Count;
	@TableField(exist=false)
	private int item2Count;
	@TableField(exist=false)
	private int item3Count;
	@TableField(exist=false)
	private int item4Count;
	//投票人员
	@TableField(exist=false)
	private List<VoteMemberEntity> voteMemberItem1EntityList;
	@TableField(exist=false)
	private List<VoteMemberEntity> voteMemberItem2EntityList;
	@TableField(exist=false)
	private List<VoteMemberEntity> voteMemberItem3EntityList;
	@TableField(exist=false)
	private List<VoteMemberEntity> voteMemberItem4EntityList;

	@TableField(exist=false)
	private Date printDate;

	/**
	 * 投票状态 1同意过半 2反对过半 3撤销 4选择确定
	 */
	@TableField(exist=false)
	private String statusCn;

	/**
	 * 小区名称
	 */
	@TableField(exist=false)
	private String zoneName;

	/**
	 * 正常弃权
	 */
	@TableField(exist=false)
	private int timeQuitNum;

	/**
	 * 超时弃权
	 */
	@TableField(exist=false)
	private int noTimeQuitNum;

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
	 * 发起人id
	 */
	private Long uid;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 参与表决人数
	 */
	private Integer num;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 选项1
	 */
	private String item1;
	/**
	 * 选项2
	 */
	private String item2;
	/**
	 * 选项3
	 */
	private String item3;
	/**
	 * 都不同意
	 */
	private String item4;
	/**
	 * 投票类型 1实名 2匿名
	 */
	private Integer type;
	/**
	 * 审批类型, 0.业委会审批 1.单人审批 2.自定义审批
	 */
	@ApiModelProperty("审批类型，0业委会审批1单人审批2自定义审批")
	private Integer verifyType;
	/**
	 * 选择类型 0单项 1多选
	 */
	private Integer itemType;
	/**
	 * 截止时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date endTime;
	/**
	 * 同意人数
	 */
	private Integer yesNum;
	/**
	 * 反对人数
	 */
	private Integer noNum;
	/**
	 * 弃权人数
	 */
	private Integer quitNum;
	/**
	 * 投票状态 1同意过半 2反对过半 3撤销 4选择确定
	 */
	private Integer status;
	/**
	 * 多数项
	 */
	private String voteItem;
	/**
	 * 添加时间
	 */
	private Date createAt;

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
	 * 设置：发起人id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：发起人id
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
	 * 设置：参与表决人数
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * 获取：参与表决人数
	 */
	public Integer getNum() {
		return num;
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
	 * 设置：选项1
	 */
	public void setItem1(String item1) {
		this.item1 = item1;
	}
	/**
	 * 获取：选项1
	 */
	public String getItem1() {
		return item1;
	}
	/**
	 * 设置：选项2
	 */
	public void setItem2(String item2) {
		this.item2 = item2;
	}
	/**
	 * 获取：选项2
	 */
	public String getItem2() {
		return item2;
	}
	/**
	 * 设置：选项3
	 */
	public void setItem3(String item3) {
		this.item3 = item3;
	}
	/**
	 * 获取：选项3
	 */
	public String getItem3() {
		return item3;
	}
	/**
	 * 设置：都不同意
	 */
	public void setItem4(String item4) {
		this.item4 = item4;
	}
	/**
	 * 获取：都不同意
	 */
	public String getItem4() {
		return item4;
	}
	/**
	 * 设置：投票类型 1实名 2匿名
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：投票类型 1实名 2匿名
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：选择类型 0单项 1多选
	 */
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	/**
	 * 获取：选择类型 0单项 1多选
	 */
	public Integer getItemType() {
		return itemType;
	}
	/**
	 * 设置：截止时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：截止时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：同意人数
	 */
	public void setYesNum(Integer yesNum) {
		this.yesNum = yesNum;
	}
	/**
	 * 获取：同意人数
	 */
	public Integer getYesNum() {
		return yesNum;
	}
	/**
	 * 设置：反对人数
	 */
	public void setNoNum(Integer noNum) {
		this.noNum = noNum;
	}
	/**
	 * 获取：反对人数
	 */
	public Integer getNoNum() {
		return noNum;
	}
	/**
	 * 设置：弃权人数
	 */
	public void setQuitNum(Integer quitNum) {
		this.quitNum = quitNum;
	}
	/**
	 * 获取：弃权人数
	 */
	public Integer getQuitNum() {
		return quitNum;
	}
	/**
	 * 设置：投票状态 1同意过半 2反对过半 3撤销 4选择确定
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：投票状态 1同意过半 2反对过半 3撤销 4选择确定
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：多数项
	 */
	public void setVoteItem(String voteItem) {
		this.voteItem = voteItem;
	}
	/**
	 * 获取：多数项
	 */
	public String getVoteItem() {
		return voteItem;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public List<VoteMemberEntity> getVoteMemberEntityList() {
		return voteMemberEntityList;
	}

	public void setVoteMemberEntityList(List<VoteMemberEntity> voteMemberEntityList) {
		this.voteMemberEntityList = voteMemberEntityList;
	}

	public List<VoteMemberEntity> getCopy2VoteMemberEntityList() {
		return copy2VoteMemberEntityList;
	}

	public void setCopy2VoteMemberEntityList(List<VoteMemberEntity> copy2VoteMemberEntityList) {
		this.copy2VoteMemberEntityList = copy2VoteMemberEntityList;
	}

	public String getTypeCn() {
		return typeCn;
	}

	public void setTypeCn(String typeCn) {
		this.typeCn = typeCn;
	}

	public int getItem1Count() {
		return item1Count;
	}

	public void setItem1Count(int item1Count) {
		this.item1Count = item1Count;
	}

	public int getItem2Count() {
		return item2Count;
	}

	public void setItem2Count(int item2Count) {
		this.item2Count = item2Count;
	}

	public int getItem3Count() {
		return item3Count;
	}

	public void setItem3Count(int item3Count) {
		this.item3Count = item3Count;
	}

	public int getItem4Count() {
		return item4Count;
	}

	public void setItem4Count(int item4Count) {
		this.item4Count = item4Count;
	}

	public List<VoteMemberEntity> getVoteMemberItem1EntityList() {
		return voteMemberItem1EntityList;
	}

	public void setVoteMemberItem1EntityList(List<VoteMemberEntity> voteMemberItem1EntityList) {
		this.voteMemberItem1EntityList = voteMemberItem1EntityList;
	}

	public List<VoteMemberEntity> getVoteMemberItem2EntityList() {
		return voteMemberItem2EntityList;
	}

	public void setVoteMemberItem2EntityList(List<VoteMemberEntity> voteMemberItem2EntityList) {
		this.voteMemberItem2EntityList = voteMemberItem2EntityList;
	}

	public List<VoteMemberEntity> getVoteMemberItem3EntityList() {
		return voteMemberItem3EntityList;
	}

	public void setVoteMemberItem3EntityList(List<VoteMemberEntity> voteMemberItem3EntityList) {
		this.voteMemberItem3EntityList = voteMemberItem3EntityList;
	}

	public List<VoteMemberEntity> getVoteMemberItem4EntityList() {
		return voteMemberItem4EntityList;
	}

	public void setVoteMemberItem4EntityList(List<VoteMemberEntity> voteMemberItem4EntityList) {
		this.voteMemberItem4EntityList = voteMemberItem4EntityList;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
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

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public Integer getVerifyType() {
		return verifyType;
	}

	public void setVerifyType(Integer verifyType) {
		this.verifyType = verifyType;
	}

	public int getTimeQuitNum() {
		return timeQuitNum;
	}

	public void setTimeQuitNum(int timeQuitNum) {
		this.timeQuitNum = timeQuitNum;
	}

	public int getNoTimeQuitNum() {
		return noTimeQuitNum;
	}

	public void setNoTimeQuitNum(int noTimeQuitNum) {
		this.noTimeQuitNum = noTimeQuitNum;
	}
}
