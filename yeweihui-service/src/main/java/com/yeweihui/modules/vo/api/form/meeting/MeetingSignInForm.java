package com.yeweihui.modules.vo.api.form.meeting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议签到")
public class MeetingSignInForm {
    @ApiModelProperty(value = "会议id")
    private Long mid;
    @ApiModelProperty(value = "签到用户id")
    private Long uid;
    @ApiModelProperty(value = "签到签名图片")
    private String verifyUrl;
}
