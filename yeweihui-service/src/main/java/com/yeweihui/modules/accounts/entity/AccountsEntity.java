package com.yeweihui.modules.accounts.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 账簿信息
 * 
 *<br/><br/>
 *用于记录小区财务账簿信息
 * @author 朱晓龙
 * 2020年8月17日  上午10:38:00
 */
@Data
@TableName("accounts")
public class AccountsEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@TableId
	private long id;
	/**
	 * 小区ID - 绑定小区信息
	 */
	@Min(value=0,message="小区ID必须大于等于0")
	private Long zoneId;
	/**
	 * 创建时间 - 账簿创建时间,默认当时时间
	 */
	@TableField(fill= FieldFill.INSERT)
	private Date createTime;
	/**
	 * 更新时间 - 账簿更新时间
	 */
	@TableField(fill=FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 记账日期 - 记账的起始月份，格式：2020-07-01，日固定为1号即可
	 */
	@NotNull(message="记账日期不能为空")
	@JsonFormat(pattern = "yyyy-MM")
	@DateTimeFormat(pattern = "yyyy-MM")
	private Date startDate;
	/**
	 * 封账日期 - 封账的截止月份，格式：2020-07-01，日固定为1号即可
	 */
	@JsonFormat(pattern = "yyyy-MM")
	@DateTimeFormat(pattern = "yyyy-MM")
	private Date endDate;
	/**
	 * 账簿状态 - 账簿状态:0(录入、默认)、1(审核中)、2(审核失败)、3(审核成功)
	 */
	@Range(min=0,max=3,message="账簿状态必须为：0(录入)、1(审核中)、2(审核失败)、3(审核成功)")
	private Integer status;
	/**
	 * 上个账簿经营结余 - 用于综合计算本账簿的经营结余
	 */
	@Min(value=0,message="上个账簿经营结余金额必须大于等于0")
	private Double lastOperatingSurplus;
	/**
	 * 上个账簿押金结余 - 用于综合计算本账簿的押金结余
	 */
	@Min(value=0,message="上个账簿押金结余金额必须大于等于0")
	private Double lastPledgeSurplus;
	/**
	 * 审核人 - 账簿的审核人员
	 */
	private String auditor;
}
