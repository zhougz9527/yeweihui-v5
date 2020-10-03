package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "财务科目信息查询条件")
public class AccountsSubjectQueryParam extends BaseQueryParam{
    
	@ApiModelProperty(value = "科目名称，模糊查询")
	private String name;
	@ApiModelProperty(value = "科目类型：1(经营)、2(押金)")
	private List<Integer> type = new ArrayList<Integer>();
	@ApiModelProperty(value = "收支类型：1(收入)、2(支出)")
	private List<Integer> rdtype = new ArrayList<Integer>();
	@ApiModelProperty(value = "绑定上级科目，用于计算科目层级")
	private List<Long> parentID = new ArrayList<Long>();
	
}
