package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "通知人员查询条件")
public class NoticeMemberQueryParam {
    @ApiModelProperty(value = "通知id")
    private Long nid;

    @ApiModelProperty(value = "状态 1未读 2已读")
    private Integer noticeMemberStatus;
}
