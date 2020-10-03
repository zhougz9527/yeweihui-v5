package com.yeweihui.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表（dq_user） 20180911
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-10-21 22:37:17
 */
@TableName("yeweihui_user")
public class DuqinUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id自增
	 */
	@TableId
	private Long id;
	/**
	 * 账号
	 */
	private String name;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 用户类型（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN） 
	 */
	private String type;
	/**
	 * 申请成为 type中的一个角色,默认NONE,不申请
	 */
	private String applyFor;
	/**
	 * 升级申请时间
	 */
	private Date applyForTime;
	/**
	 * 手机
	 */
	private String phone;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 用户头像url
	 */
	private String headIconUrl;
	/**
	 * 注册时间
	 */
	private Date registerTime;
	/**
	 * 最后一次登陆时间
	 */
	private Date lastLoginTime;
	/**
	 * 累计浏览次数
	 */
	private Integer viewTimes;
	/**
	 * 累计评论次数
	 */
	private Integer commentTimes;
	/**
	 * 累计发微读数
	 */
	private Integer weReadTimes;
	/**
	 * 累计小视频数
	 */
	private Integer tinyVideoCount;
	/**
	 * 累计视频数
	 */
	private Integer videoCount;
	/**
	 * 累计歌曲数
	 */
	private Integer songCount;
	/**
	 * 累计文章数
	 */
	private Integer articleCount;
	/**
	 * 累计图集数
	 */
	private Integer picGroupCount;
	/**
	 * 手机唯一识别码
	 */
	private String phoneUniqueCode;
	/**
	 * token
	 */
	private String token;
	/**
	 * 创建时间
	 */
    @TableField(fill= FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改时间
	 */
	@TableField(fill=FieldFill.INSERT_UPDATE)
	private Date modifyTime;
	/**
	 * 用户生日
	 */
	private Date birthday;
	/**
	 * 是否冻结
	 */
	private String isFreeze;
	/**
	 * 冻结原因
	 */
	private String freezeReason;
	/**
	 * 是否禁言
	 */
	private String isMute;
	/**
	 * 禁言原因
	 */
	private String muteReason;
	/**
	 * 用户介绍
	 */
	private String description;
	/**
	 * app转发次数
	 */
	private Integer appShareCount;
	/**
	 * 微信转发次数
	 */
	private Integer wechatShareCount;
	/**
	 * 朋友圈转发次数
	 */
	private Integer momentsShareCount;
	/**
	 * 微博转发次数
	 */
	private Integer microBlogShareCount;
	/**
	 * qq转发次数
	 */
	private Integer qqShareCount;
	/**
	 * app今日阅读次数
	 */
	private Integer todayReadCount;
	/**
	 * app累计阅读次数
	 */
	private Integer totalReadCount;
	/**
	 * app平均日阅读次数
	 */
	private Integer avgDayReadCount;
	/**
	 * 今日停留（分钟）
	 */
	private Long todayStayMinutes;
	/**
	 * 累计停留（分钟）
	 */
	private Long totalStayMinutes;
	/**
	 * 使用app天数(使用一次一天累加一次)
	 */
	private Integer totalAppUseDays;
	/**
	 * 地区
	 */
	private String area;
	/**
	 * MALE,FEMALE
	 */
	private String gender;

	/**
	 * 设置：id自增
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id自增
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：账号
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：账号
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：用户类型（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN） 
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：用户类型（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN） 
	 */
	public String getType() {
		return type;
	}
	/**
	 * 设置：申请成为 type中的一个角色,默认NONE,不申请
	 */
	public void setApplyFor(String applyFor) {
		this.applyFor = applyFor;
	}
	/**
	 * 获取：申请成为 type中的一个角色,默认NONE,不申请
	 */
	public String getApplyFor() {
		return applyFor;
	}
	/**
	 * 设置：升级申请时间
	 */
	public void setApplyForTime(Date applyForTime) {
		this.applyForTime = applyForTime;
	}
	/**
	 * 获取：升级申请时间
	 */
	public Date getApplyForTime() {
		return applyForTime;
	}
	/**
	 * 设置：手机
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：昵称
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * 获取：昵称
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * 设置：用户头像url
	 */
	public void setHeadIconUrl(String headIconUrl) {
		this.headIconUrl = headIconUrl;
	}
	/**
	 * 获取：用户头像url
	 */
	public String getHeadIconUrl() {
		return headIconUrl;
	}
	/**
	 * 设置：注册时间
	 */
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	/**
	 * 获取：注册时间
	 */
	public Date getRegisterTime() {
		return registerTime;
	}
	/**
	 * 设置：最后一次登陆时间
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	/**
	 * 获取：最后一次登陆时间
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * 设置：累计浏览次数
	 */
	public void setViewTimes(Integer viewTimes) {
		this.viewTimes = viewTimes;
	}
	/**
	 * 获取：累计浏览次数
	 */
	public Integer getViewTimes() {
		return viewTimes;
	}
	/**
	 * 设置：累计评论次数
	 */
	public void setCommentTimes(Integer commentTimes) {
		this.commentTimes = commentTimes;
	}
	/**
	 * 获取：累计评论次数
	 */
	public Integer getCommentTimes() {
		return commentTimes;
	}
	/**
	 * 设置：累计发微读数
	 */
	public void setWeReadTimes(Integer weReadTimes) {
		this.weReadTimes = weReadTimes;
	}
	/**
	 * 获取：累计发微读数
	 */
	public Integer getWeReadTimes() {
		return weReadTimes;
	}
	/**
	 * 设置：累计小视频数
	 */
	public void setTinyVideoCount(Integer tinyVideoCount) {
		this.tinyVideoCount = tinyVideoCount;
	}
	/**
	 * 获取：累计小视频数
	 */
	public Integer getTinyVideoCount() {
		return tinyVideoCount;
	}
	/**
	 * 设置：累计视频数
	 */
	public void setVideoCount(Integer videoCount) {
		this.videoCount = videoCount;
	}
	/**
	 * 获取：累计视频数
	 */
	public Integer getVideoCount() {
		return videoCount;
	}
	/**
	 * 设置：累计歌曲数
	 */
	public void setSongCount(Integer songCount) {
		this.songCount = songCount;
	}
	/**
	 * 获取：累计歌曲数
	 */
	public Integer getSongCount() {
		return songCount;
	}
	/**
	 * 设置：累计文章数
	 */
	public void setArticleCount(Integer articleCount) {
		this.articleCount = articleCount;
	}
	/**
	 * 获取：累计文章数
	 */
	public Integer getArticleCount() {
		return articleCount;
	}
	/**
	 * 设置：累计图集数
	 */
	public void setPicGroupCount(Integer picGroupCount) {
		this.picGroupCount = picGroupCount;
	}
	/**
	 * 获取：累计图集数
	 */
	public Integer getPicGroupCount() {
		return picGroupCount;
	}
	/**
	 * 设置：手机唯一识别码
	 */
	public void setPhoneUniqueCode(String phoneUniqueCode) {
		this.phoneUniqueCode = phoneUniqueCode;
	}
	/**
	 * 获取：手机唯一识别码
	 */
	public String getPhoneUniqueCode() {
		return phoneUniqueCode;
	}
	/**
	 * 设置：token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 获取：token
	 */
	public String getToken() {
		return token;
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
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：用户生日
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	/**
	 * 获取：用户生日
	 */
	public Date getBirthday() {
		return birthday;
	}
	/**
	 * 设置：是否冻结
	 */
	public void setIsFreeze(String isFreeze) {
		this.isFreeze = isFreeze;
	}
	/**
	 * 获取：是否冻结
	 */
	public String getIsFreeze() {
		return isFreeze;
	}
	/**
	 * 设置：冻结原因
	 */
	public void setFreezeReason(String freezeReason) {
		this.freezeReason = freezeReason;
	}
	/**
	 * 获取：冻结原因
	 */
	public String getFreezeReason() {
		return freezeReason;
	}
	/**
	 * 设置：是否禁言
	 */
	public void setIsMute(String isMute) {
		this.isMute = isMute;
	}
	/**
	 * 获取：是否禁言
	 */
	public String getIsMute() {
		return isMute;
	}
	/**
	 * 设置：禁言原因
	 */
	public void setMuteReason(String muteReason) {
		this.muteReason = muteReason;
	}
	/**
	 * 获取：禁言原因
	 */
	public String getMuteReason() {
		return muteReason;
	}
	/**
	 * 设置：用户介绍
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：用户介绍
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：app转发次数
	 */
	public void setAppShareCount(Integer appShareCount) {
		this.appShareCount = appShareCount;
	}
	/**
	 * 获取：app转发次数
	 */
	public Integer getAppShareCount() {
		return appShareCount;
	}
	/**
	 * 设置：微信转发次数
	 */
	public void setWechatShareCount(Integer wechatShareCount) {
		this.wechatShareCount = wechatShareCount;
	}
	/**
	 * 获取：微信转发次数
	 */
	public Integer getWechatShareCount() {
		return wechatShareCount;
	}
	/**
	 * 设置：朋友圈转发次数
	 */
	public void setMomentsShareCount(Integer momentsShareCount) {
		this.momentsShareCount = momentsShareCount;
	}
	/**
	 * 获取：朋友圈转发次数
	 */
	public Integer getMomentsShareCount() {
		return momentsShareCount;
	}
	/**
	 * 设置：微博转发次数
	 */
	public void setMicroBlogShareCount(Integer microBlogShareCount) {
		this.microBlogShareCount = microBlogShareCount;
	}
	/**
	 * 获取：微博转发次数
	 */
	public Integer getMicroBlogShareCount() {
		return microBlogShareCount;
	}
	/**
	 * 设置：qq转发次数
	 */
	public void setQqShareCount(Integer qqShareCount) {
		this.qqShareCount = qqShareCount;
	}
	/**
	 * 获取：qq转发次数
	 */
	public Integer getQqShareCount() {
		return qqShareCount;
	}
	/**
	 * 设置：app今日阅读次数
	 */
	public void setTodayReadCount(Integer todayReadCount) {
		this.todayReadCount = todayReadCount;
	}
	/**
	 * 获取：app今日阅读次数
	 */
	public Integer getTodayReadCount() {
		return todayReadCount;
	}
	/**
	 * 设置：app累计阅读次数
	 */
	public void setTotalReadCount(Integer totalReadCount) {
		this.totalReadCount = totalReadCount;
	}
	/**
	 * 获取：app累计阅读次数
	 */
	public Integer getTotalReadCount() {
		return totalReadCount;
	}
	/**
	 * 设置：app平均日阅读次数
	 */
	public void setAvgDayReadCount(Integer avgDayReadCount) {
		this.avgDayReadCount = avgDayReadCount;
	}
	/**
	 * 获取：app平均日阅读次数
	 */
	public Integer getAvgDayReadCount() {
		return avgDayReadCount;
	}
	/**
	 * 设置：今日停留（分钟）
	 */
	public void setTodayStayMinutes(Long todayStayMinutes) {
		this.todayStayMinutes = todayStayMinutes;
	}
	/**
	 * 获取：今日停留（分钟）
	 */
	public Long getTodayStayMinutes() {
		return todayStayMinutes;
	}
	/**
	 * 设置：累计停留（分钟）
	 */
	public void setTotalStayMinutes(Long totalStayMinutes) {
		this.totalStayMinutes = totalStayMinutes;
	}
	/**
	 * 获取：累计停留（分钟）
	 */
	public Long getTotalStayMinutes() {
		return totalStayMinutes;
	}
	/**
	 * 设置：使用app天数(使用一次一天累加一次)
	 */
	public void setTotalAppUseDays(Integer totalAppUseDays) {
		this.totalAppUseDays = totalAppUseDays;
	}
	/**
	 * 获取：使用app天数(使用一次一天累加一次)
	 */
	public Integer getTotalAppUseDays() {
		return totalAppUseDays;
	}
	/**
	 * 设置：地区
	 */
	public void setArea(String area) {
		this.area = area;
	}
	/**
	 * 获取：地区
	 */
	public String getArea() {
		return area;
	}
	/**
	 * 设置：MALE,FEMALE
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * 获取：MALE,FEMALE
	 */
	public String getGender() {
		return gender;
	}
}
