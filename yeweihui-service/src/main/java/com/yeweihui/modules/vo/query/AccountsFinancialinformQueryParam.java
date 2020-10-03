package com.yeweihui.modules.vo.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(value = "财务收支信息查询条件")
public class AccountsFinancialinformQueryParam extends BaseQueryParam {
	@ApiModelProperty(value = "凭证ID")
	@Min(value = 1, message="凭证ID必须为大于0的数值")
	private Long voucherId;
	@ApiModelProperty(value = "财务科目ID")
	private List<Integer> subjectIds = new ArrayList<Integer>();
	@ApiModelProperty(value = "摘要信息")
	private String digest;
	@ApiModelProperty(value = "辅助账信息")
	private String auxiliary;
	@ApiModelProperty(value = "指定最小金额,为null或小于等于0,不参与查询")
	private Double minMoney;
	@ApiModelProperty(value = "指定最大金额,为null或小于等于0,不参与查询")
	private Double maxMoney;
	@ApiModelProperty(value = "凭证日期，格式：yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@ApiModelProperty(value = "凭证日期，格式：yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	@ApiModelProperty(value = "记字号数字")
	@Min(value = 0, message="记字号数字必须为大于0的数值")
	private Long tagNumber;
	@ApiModelProperty(value = "小区ID - 绑定小区信息")
	@Min(value = 0, message="小区ID必须为大于等于0的数值")
	private Long zoneId;
	@ApiModelProperty(value = "科目ID - 绑定科目信息")
	@Min(value = 0, message="科目ID必须为大于等于0的数值")
	private Long subjectId;
}
