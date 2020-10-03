package com.yeweihui.modules.vo.api.form.meeting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议签字")
public class MeetingSignForm {
    @ApiModelProperty(value = "会议id")
    private Long mid;
    @ApiModelProperty(value = "会议签字用户id")
    private Long uid;
    @ApiModelProperty(value = "会议签字url")
    private String signEndUrl;
}
