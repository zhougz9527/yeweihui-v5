package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "投票成员查询条件")
public class VoteMemberQueryParam {
    @ApiModelProperty(value = "表决id")
    private Long vid;
    @ApiModelProperty(value = "类型 1表决 2抄送")
    private Integer voteMemberType;
    @ApiModelProperty(value = "状态 0未投票 1同意 2反对 3弃权 4超时")
    private Integer voteMemberStatus;

    @ApiModelProperty(value = "哪个选项 item1 - item4")
    private String itemChoice;
}
