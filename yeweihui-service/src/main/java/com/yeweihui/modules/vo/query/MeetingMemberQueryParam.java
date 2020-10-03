package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "会议成员查询条件")
public class MeetingMemberQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "会议id")
    private Long mid;
    @ApiModelProperty(value = "会议参与类型 1参会 2抄送")
    private Integer meetingMemberType;
    @ApiModelProperty(value = "会议参与类型 1参会 2抄送")
    private Integer meetingMemberStatus;
    @ApiModelProperty(value = "会议参与用户id")
    private Long uid;
    @ApiModelProperty(value = "签名是否")
    private String signFlag;
}
