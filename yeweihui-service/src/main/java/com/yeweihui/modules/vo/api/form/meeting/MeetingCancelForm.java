package com.yeweihui.modules.vo.api.form.meeting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议撤销")
public class MeetingCancelForm {
    @ApiModelProperty(value = "会议id")
    private Long mid;
    @ApiModelProperty(value = "会议撤销用户id")
    private Long uid;
}
