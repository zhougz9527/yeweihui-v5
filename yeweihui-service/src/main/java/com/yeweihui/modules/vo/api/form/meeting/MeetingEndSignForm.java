package com.yeweihui.modules.vo.api.form.meeting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议结束签字")
public class MeetingEndSignForm {
    @ApiModelProperty(value = "会议id")
    private Long mid;
    @ApiModelProperty(value = "会议结束签字用户id")
    private Long uid;
    @ApiModelProperty(value = "是否发起人签字（发起人签字结束会议）")
    private boolean referUserSign;
}
