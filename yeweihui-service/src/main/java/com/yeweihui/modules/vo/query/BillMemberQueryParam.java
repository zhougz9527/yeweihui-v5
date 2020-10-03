package com.yeweihui.modules.vo.query;

import com.yeweihui.modules.enums.bill.BillMemberStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "报销成员查询条件")
public class BillMemberQueryParam {
    @ApiModelProperty(value = "报销id")
    private Long bid;
    @ApiModelProperty(value = "报销审核用户id")
    private Long uid;
    @ApiModelProperty(value = "报销审核状态")
    private Integer billMemberStatus;
    @ApiModelProperty(value = "类型 1审批 2抄送")
    private Integer billMemberType;
}
