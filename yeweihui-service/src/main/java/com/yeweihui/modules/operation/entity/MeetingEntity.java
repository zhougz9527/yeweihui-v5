package com.yeweihui.modules.operation.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 会议表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@TableName("meeting")
@ApiModel(value = "会议表")
public class MeetingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	@ApiModelProperty(value = "会议参与成员")
	List<MeetingMemberEntity> meetingMemberEntityList;

	@TableField(exist = false)
	@ApiModelProperty(value = "会议抄送成员")
	List<MeetingMemberEntity> copy2MeetingMemberEntityList;

	@TableField(exist = false)
	@ApiModelProperty(value = "会议记录")
	List<MeetingLogEntity> meetingLogEntityList;

	@TableField(exist = false)
	@ApiModelProperty(value = "会议参与状态")
	private Integer memberStatus;

	@TableField(exist = false)
	@ApiModelProperty(value = "参会用户id，不区参会分抄送")//memberUid 和 mid去重
	private Long memberUid;

	@TableField(exist = false)
	@ApiModelProperty(value = "会议参与状态中文")
	private String memberStatusCn;

	@TableField(exist = false)
	@ApiModelProperty(value = "发起用户真实姓名")
	private String realname;

	@TableField(exist = false)
	@ApiModelProperty(value = "发起用户头像url")
	private String avatarUrl;

	@TableField(exist = false)
	@ApiModelProperty(value = "签到签名url")
	private String verifyUrl;

	@TableField(exist = false)
	@ApiModelProperty(value = "小区名称")
	private String zoneName;

	@TableField(exist = false)
	@ApiModelProperty(value = "状态 0待签到 1进行中 2待签字 3结束 4取消")
	private String statusCn;

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
	 * 发起人
	 */
	private Long uid;
	/**
	 * 小区
	 */
	private Long zoneId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 参加人数
	 */
	private Integer num;
	/**
	 * 签到人数
	 */
	private Integer signNum;
	/**
	 * 会议地点
	 */
	private String location;
	/**
	 * 开始时间
	 */
	private Date startAt;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 耗时多久秒
	 */
	private String spendTime;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 状态 0待签到 1进行中 2待签字 3结束 4取消
	 */
	private Integer status;

	/**
	 * 打印日期 今天
	 */
	@TableField(exist = false)
	private Date printDate;

	/**
	 * 记录状态 0删除 1业主不显示 2通过
	 */
	private Integer recordStatus;

	/**
	 * 开会类型 0正常开会 1现场开会
	 */
	private Integer type;

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
	 * 设置：发起人
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * 获取：发起人
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * 设置：小区
	 */
	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}
	/**
	 * 获取：小区
	 */
	public Long getZoneId() {
		return zoneId;
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
	 * 设置：参加人数
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * 获取：参加人数
	 */
	public Integer getNum() {
		return num;
	}
	/**
	 * 设置：签到人数
	 */
	public void setSignNum(Integer signNum) {
		this.signNum = signNum;
	}
	/**
	 * 获取：签到人数
	 */
	public Integer getSignNum() {
		return signNum;
	}
	/**
	 * 设置：会议地点
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * 获取：会议地点
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * 设置：开始时间
	 */
	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	/**
	 * 获取：开始时间
	 */
	public Date getStartAt() {
		return startAt;
	}

	public String getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(String spendTime) {
		this.spendTime = spendTime;
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
	 * 设置：状态 0待签到 1进行中 2待签字 3结束 4取消
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0待签到 1进行中 2待签字 3结束 4取消
	 */
	public Integer getStatus() {
		return status;
	}

	public List<MeetingMemberEntity> getMeetingMemberEntityList() {
		return meetingMemberEntityList;
	}

	public void setMeetingMemberEntityList(List<MeetingMemberEntity> meetingMemberEntityList) {
		this.meetingMemberEntityList = meetingMemberEntityList;
	}

	public List<MeetingLogEntity> getMeetingLogEntityList() {
		return meetingLogEntityList;
	}

	public void setMeetingLogEntityList(List<MeetingLogEntity> meetingLogEntityList) {
		this.meetingLogEntityList = meetingLogEntityList;
	}

	public List<MeetingMemberEntity> getCopy2MeetingMemberEntityList() {
		return copy2MeetingMemberEntityList;
	}

	public void setCopy2MeetingMemberEntityList(List<MeetingMemberEntity> copy2MeetingMemberEntityList) {
		this.copy2MeetingMemberEntityList = copy2MeetingMemberEntityList;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
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

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getStatusCn() {
		return statusCn;
	}

	public void setStatusCn(String statusCn) {
		this.statusCn = statusCn;
	}

	public String getMemberStatusCn() {
		return memberStatusCn;
	}

	public void setMemberStatusCn(String memberStatusCn) {
		this.memberStatusCn = memberStatusCn;
	}

	public Long getMemberUid() {
		return memberUid;
	}

	public void setMemberUid(Long memberUid) {
		this.memberUid = memberUid;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}
}
