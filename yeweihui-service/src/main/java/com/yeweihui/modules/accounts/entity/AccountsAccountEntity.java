package com.yeweihui.modules.accounts.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;

import lombok.Data;

/**
 * 账户信息
 * 
 *<br/><br/>
 *用于一对一保存小区银行账户的相关信息
 * @author 朱晓龙
 * 2020年8月17日  下午1:43:42
 */
@Data
@TableName("accounts_account")
public class AccountsAccountEntity implements Serializable{

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
	private long zoneId;
	/**
	 * 更新时间 - 凭证更新时间
	 */
	@TableField(fill=FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 开户名 - 开户名称
	 */
	@Length(max=50,message="开户名称不能超过50个字符")
	private String name;
	/**
	 * 开户行 - 开户银行
	 */
	@Length(max=200,message="开户银行不能超过200个字符")
	private String bankname;
	/**
	 * 开户账号 - 开户账号
	 */
	@Length(max=50,message="开户账号不能超过50个字符")
	private String cardnumber;
	
}
