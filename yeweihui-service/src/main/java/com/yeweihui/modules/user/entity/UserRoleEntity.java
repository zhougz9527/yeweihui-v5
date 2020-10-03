package com.yeweihui.modules.user.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
@TableName("user_role")
public class UserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 编码
	 */
	private Integer code;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 对应后台角色
	 */
	private Integer roleId;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：编码
	 */
	public void setCode(Integer code) {
		this.code = code;
	}
	/**
	 * 获取：编码
	 */
	public Integer getCode() {
		return code;
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
	 * 设置：对应后台角色
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	/**
	 * 获取：对应后台角色
	 */
	public Integer getRoleId() {
		return roleId;
	}
}
