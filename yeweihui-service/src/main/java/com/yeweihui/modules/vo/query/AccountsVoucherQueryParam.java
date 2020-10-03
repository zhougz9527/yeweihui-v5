package com.yeweihui.modules.vo.query;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "凭证信息查询条件")
public class AccountsVoucherQueryParam extends BaseQueryParam{
	
	@ApiModelProperty(value = "账簿ID")
	@NotNull(message="账簿ID不能为空")
	@Min(value = 0, message="账簿ID必须为大于等于0的数值")
	private Long accountsId;
	@ApiModelProperty(value = "凭证日期，格式：yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Date date;
	@ApiModelProperty(value = "记字号数字")
	@Min(value = 0, message="记字号数字必须为大于0的数值")
	private Long tagNumber;
	@ApiModelProperty(value = "科目ID")
	private List<Long> subjectId = new ArrayList<Long>();
}
