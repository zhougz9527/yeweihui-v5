package com.yeweihui.modules.user.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeweihui.modules.bfly.entity.BflyRoom;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 小区
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
@TableName("zones")
public class ZonesEntity implements Serializable {
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
	 * 小区名称
	 */
	private String name;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 前台电话
	 */
	private String tel;
	/**
	 * 状态 0未审批 1通过 2未通过
	 */
	private Integer status;
	/**
	 * 省id
	 */
	private Long provinceId;
	/**
	 * 省
	 */
	private String provinceName;
	/**
	 * 市id
	 */
	private Long cityId;
	/**
	 * 市
	 */
	private String cityName;
	/**
	 * 区id
	 */
	private Long districtId;
	/**
	 * 区
	 */
	private String districtName;
	/**
	 * 邀请码
	 */
	private Long subdistrictId;
	private String subdistrictName;

	private Long communityId;
	private String communityName;

	private String inviteCode;
	/**
	 * 注册使用时间
	 */
	//	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date registerTime;
	/**
	 * 可全额退款时间
	 */
	//	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date refundEnableTime;
	/**
	 * 使用截止时间
	 */
	//	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	private Date enableUseTime;

	// 小区所属的房屋list
	@TableField(exist=false)
	private List<BflyRoom> bflyRoomList;

	// 小区是否有苑结构
	@TableField(exist=false)
	private Boolean courtStructure;

	// 认证的面积
	@TableField(exist = false)
	private String certAreaStr;

	public String getCertAreaStr() {
		return certAreaStr;
	}

	public void setCertAreaStr(String certAreaStr) {
		this.certAreaStr = certAreaStr;
	}

	public String getCertNumStr() {
		return certNumStr;
	}

	public void setCertNumStr(String certNumStr) {
		this.certNumStr = certNumStr;
	}

	// 认证的户数
	@TableField(exist = false)
	private String certNumStr;

	public List<BflyRoom> getBflyRoomList() {
		return bflyRoomList;
	}

	public void setBflyRoomList(List<BflyRoom> bflyRoomList) {
		this.bflyRoomList = bflyRoomList;
	}

	public Boolean getCourtStructure() {
		return courtStructure;
	}

	public void setCourtStructure(Boolean courtStructure) {
		this.courtStructure = courtStructure;
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
	 * 设置：小区名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：小区名称
	 */
	public String getName() {
		return name;
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
	 * 设置：前台电话
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * 获取：前台电话
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * 设置：状态 0未审批 1通过 2未通过
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0未审批 1通过 2未通过
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：省id
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	/**
	 * 获取：省id
	 */
	public Long getProvinceId() {
		return provinceId;
	}
	/**
	 * 设置：省
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	/**
	 * 获取：省
	 */
	public String getProvinceName() {
		return provinceName;
	}
	/**
	 * 设置：市id
	 */
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	/**
	 * 获取：市id
	 */
	public Long getCityId() {
		return cityId;
	}
	/**
	 * 设置：市
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	/**
	 * 获取：市
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * 设置：区id
	 */
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	/**
	 * 获取：区id
	 */
	public Long getDistrictId() {
		return districtId;
	}
	/**
	 * 设置：区
	 */
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	/**
	 * 获取：区
	 */
	public String getDistrictName() {
		return districtName;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Date getRefundEnableTime() {
		return refundEnableTime;
	}

	public void setRefundEnableTime(Date refundEnableTime) {
		this.refundEnableTime = refundEnableTime;
	}

	public Date getEnableUseTime() {
		return enableUseTime;
	}

	public void setEnableUseTime(Date enableUseTime) {
		this.enableUseTime = enableUseTime;
	}

	public Long getSubdistrictId() {
		return subdistrictId;
	}

	public void setSubdistrictId(Long subdistrictId) {
		this.subdistrictId = subdistrictId;
	}

	public String getSubdistrictName() {
		return subdistrictName;
	}

	public void setSubdistrictName(String subdistrictName) {
		this.subdistrictName = subdistrictName;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
}
