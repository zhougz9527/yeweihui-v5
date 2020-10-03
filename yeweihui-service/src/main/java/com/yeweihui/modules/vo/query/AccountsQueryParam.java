package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@ApiModel(value = "账簿信息查询条件")
public class AccountsQueryParam extends BaseQueryParam{
    
	@ApiModelProperty(value = "账簿ID")
	@Min(value = 0, message="账簿ID必须为大于等于0的数值")
	private Long id;
	@ApiModelProperty(value = "账簿所属月份日期，格式：yyyy-MM")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM")
	private Date accountsDate;
	@ApiModelProperty(value = "小区ID - 绑定小区信息")
	@Min(value = 0, message="小区ID必须为大于等于0的数值")
	private Long zoneId;
	@ApiModelProperty(value = "账簿状态:0(录入、默认)、1(审核中)、2(审核失败)、3(审核成功)")
	private List<Integer> status = new ArrayList<Integer>();
	@ApiModelProperty(value = "按年统计")
	private Integer yearDate;
	
}
