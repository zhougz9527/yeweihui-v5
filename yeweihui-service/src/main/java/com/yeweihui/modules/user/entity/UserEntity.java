package com.yeweihui.modules.user.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yeweihui.common.validator.group.AddGroup;
import com.yeweihui.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 业委会用户
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-04 22:08:24
 */
@TableName("user")
public class UserEntity implements Serializable {
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
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 角色code
	 */
	private String roleCode;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * openid
	 */
	private String openid;
	/**
	 * 上线uid
	 */
	private Integer pid;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 真实姓名
	 */
	private String realname;
	/**
	 * 性别 1男 2女
	 */
	private Integer gender;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 积分
	 */
	private Integer integral;
	/**
	 * 头像
	 */
	private String avatarUrl;
	/**
	 * 上次登录
	 */
	private Date lastLogin;
	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;
	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 状态 0禁用 1正常
	 */
	private Integer status;
	/**
	 * 部门ID
	 */
//	@NotNull(message="部门不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long deptId;

	@TableField(exist=false)
	private String deptName;

	/**
	 * 盐
	 */
	private String salt;
	/**
	 * 邮箱
	 */
//	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;
	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<String> roleNameList;

	/**
	 * 小区名称
	 */
	@TableField(exist=false)
	private String zoneName;

	/**
	 * 小区邀请码
	 */
	@TableField(exist=false)
	private String zoneInviteCode;

	/**
	 * 房间号
	 */
	private String roomNum;

	/**
	 * 审核状态
	 */
	private Integer verifyStatus;

	/**
	 * 是否为小区管理员
	 */
	private Boolean isManager;

	/**
	 * 角色组
	 */
	@TableField(exist = false)
	private String group;

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
	 * 设置：角色id
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：角色id
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * 设置：角色code
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	/**
	 * 获取：角色code
	 */
	public String getRoleCode() {
		return roleCode;
	}
	/**
	 * 设置：角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * 获取：角色名称
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * 设置：openid
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * 获取：openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * 设置：上线uid
	 */
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	/**
	 * 获取：上线uid
	 */
	public Integer getPid() {
		return pid;
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
	 * 设置：真实姓名
	 */
	public void setRealname(String realname) {
		this.realname = realname;
	}
	/**
	 * 获取：真实姓名
	 */
	public String getRealname() {
		return realname;
	}
	/**
	 * 设置：性别 1男 2女
	 */
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	/**
	 * 获取：性别 1男 2女
	 */
	public Integer getGender() {
		return gender;
	}
	/**
	 * 设置：市
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：市
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置：省
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取：省
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：国家
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * 获取：国家
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * 设置：积分
	 */
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	/**
	 * 获取：积分
	 */
	public Integer getIntegral() {
		return integral;
	}
	/**
	 * 设置：头像
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	/**
	 * 获取：头像
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}
	/**
	 * 设置：上次登录
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	/**
	 * 获取：上次登录
	 */
	public Date getLastLogin() {
		return lastLogin;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
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
	 * 设置：手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：手机
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：状态 0禁用 1正常
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0禁用 1正常
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：部门ID
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门ID
	 */
	public Long getDeptId() {
		return deptId;
	}
	/**
	 * 设置：盐
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	/**
	 * 获取：盐
	 */
	public String getSalt() {
		return salt;
	}
	/**
	 * 设置：邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * 获取：邮箱
	 */
	public String getEmail() {
		return email;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public List<String> getRoleNameList() {
		return roleNameList;
	}

	public void setRoleNameList(List<String> roleNameList) {
		this.roleNameList = roleNameList;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public Integer getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(Integer verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Boolean getManager() {
		return isManager;
	}

	public void setManager(Boolean manager) {
		isManager = manager;
	}

	public String getZoneInviteCode() {
		return zoneInviteCode;
	}

	public void setZoneInviteCode(String zoneInviteCode) {
		this.zoneInviteCode = zoneInviteCode;
	}
}
