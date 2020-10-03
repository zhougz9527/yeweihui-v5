package com.yeweihui.modules.accounts.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 账簿凭证
 * 
 *<br/><br/>
 *用于关联账簿和多条财务信息
 * @author 朱晓龙
 * 2020年8月17日  下午2:10:15
 */
@Data
@TableName("accounts_voucher")
public class AccountsVoucherEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private long id;
	/**
	 * 账簿ID - 绑定账簿信息
	 */
	@Min(value=0,message="账簿ID必须大于等于0")
	private long accountsId;
	/**
	 * 创建时间 - 凭证创建时间
	 */
	@TableField(fill=FieldFill.INSERT)
	private Date createTime;
	/**
	 * 更新时间 - 凭证更新时间
	 */
	@TableField(fill=FieldFill.INSERT_UPDATE)
	private Date updateTime;
	/**
	 * 凭证日期 - 指定凭证的日期
	 */
	@NotNull(message="凭证日期不能为空")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date date;
	/**
	 * 记字号数字 - 记字号数字，用于获取下一位记字号数字，也由生成记字号信息（记-[记字号数字]）
	 */
	@Min(value=1,message="记字号数字必须大于0")
	private long tagNumber;
	/**
	 * 制单人 - 凭证制作人员的名字
	 */
	@Length(max=50,message="制单人名称不能超过50个字符")
	@NotBlank(message="制单人名称不能为空")
	private String makeUsername;
	
	
	/**
	 * 财务信息集合
	 */
	@TableField(exist=false)
	private List<AccountsFinancialinformEntity> accountsFinancialinforms;
}
