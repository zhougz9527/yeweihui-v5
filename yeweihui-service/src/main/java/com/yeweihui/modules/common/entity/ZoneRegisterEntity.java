package com.yeweihui.modules.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.yeweihui.modules.enums.PlatEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 小区登记
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-10-19 17:14:58
 */
@TableName("zone_register")
public class ZoneRegisterEntity implements Serializable {
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
	 * 名字
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 小区名称
	 */
	private String zoneName;
	/**
	 * 发起的平台
	 */
	private Integer plat;

	@TableField(exist = false)
	private Long userId;

	@TableField(exist = false)
	private String openId;

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
	 * 设置：名字
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：名字
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：手机号
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：小区名称
	 */
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	/**
	 * 获取：小区名称
	 */
	public String getZoneName() {
		return zoneName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Integer getPlat() {
		return plat;
	}

	public void setPlat(Integer plat) {
		this.plat = plat;
	}
}
