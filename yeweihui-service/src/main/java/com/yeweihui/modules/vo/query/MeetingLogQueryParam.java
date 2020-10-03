package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议记录查询条件")
public class MeetingLogQueryParam {
    @ApiModelProperty(value = "会议id")
    private Long mid;
}
