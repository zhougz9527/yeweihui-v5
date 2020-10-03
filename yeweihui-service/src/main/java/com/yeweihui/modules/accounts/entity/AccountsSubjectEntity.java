package com.yeweihui.modules.accounts.entity;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * 科目信息
 * 
 *<br/><br/>
 *包含收支类型的科目信息
 * @author 朱晓龙
 * 2020年8月17日  下午1:50:34
 */
@Data
@TableName("accounts_subject")
public class AccountsSubjectEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * ID
	 */
	@TableId
	private long id;
	/**
	 * 科目名称 - 科目的名称
	 */
	@Length(max=200,message="科目名称不能超过200个字符")
	@NotBlank(message="科目名称不能为空")
	private String name;
	/**
	 * 科目类型 - 科目类型：1(经营)、2(押金)
	 */
	@Range(min=1,max=2,message="科目类型必须为：1(经营)、2(押金)")
	private Integer type;
	/**
	 * 收支类型 - 收支类型：1(收入)、2(支出),收支(receipts and disbursements)
	 */
	@Range(min=1,max=2,message="收支类型必须为：1(收入)、2(支出)")
	private Integer rdtype;
	/**
	 * 上级科目ID - 绑定上级科目，用于计算科目层级
	 */
	@Min(value=0,message="上级科目ID必须大于等于0")
	private long parentId;
	
	/**
	 * 下级科目
	 */
	@TableField(exist = false)
	private List<AccountsSubjectEntity> children;
	/**
	 * 科目类型描述
	 */
	@TableField(exist=false)
	private String typeDescribe;
	public String getTypeDescribe(){
		return String.format("%s%s", 
			(this.type != null)?(this.type == 1)?"经营":(this.type == 2)?"押金":"未知":null,
			(this.rdtype != null)?(this.rdtype == 1)?"收入":(this.rdtype == 2)?"支出":"未知":null
		);
	}
	/**
	 * 科目层级信息
	 */
	@TableField(exist=false)
	private String levelInfo;
	
}
