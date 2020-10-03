package com.yeweihui.modules.accounts.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yeweihui.modules.vo.api.vo.FileInfoVO;

import lombok.Data;
/**
 * 财务信息
 * 
 *<br/><br/>
 *记录科目的财务收支信息
 * @author 朱晓龙
 * 2020年8月17日  下午2:00:08
 */
@Data
@TableName("accounts_financialinform")
public class AccountsFinancialinformEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	private Long id;
	/**
	 * 凭证ID - 绑定凭证信息
	 */
	@Min(value=0,message="凭证ID必须大于等于0")
	private Long voucherId;
	/**
	 * 摘要 - 财务收支的摘要信息
	 */
	@Length(max=500,message="科目名称不能超过500个字符")
	private String digest;
	/**
	 * 科目ID - 绑定科目信息
	 */
	@Min(value=0,message="科目ID必须大于等于0")
	private Long subjectId;
	/**
	 * 辅助账 - 辅助账信息
	 */
	@Length(max=500,message="辅助账不能超过500个字符")
	private String auxiliary;
	/**
	 * 金额 - 金额，默认0
	 */
	@Min(value=0,message="金额 必须大于等于0")
	private Double money;
	
	
	/**
	 * 科目信息
	 */
	@TableField(exist=false)
	private AccountsSubjectEntity accountsSubject;
	/**
	 * 附件信息
	 */
	@TableField(exist=false)
	private FileInfoVO accessory;
}
