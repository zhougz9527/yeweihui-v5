package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "会议查询条件")
public class MeetingQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人）")
    private String keyword;

    @ApiModelProperty(value = "会议发起人id")
    private Long uid;
    @ApiModelProperty(value = "会议状态 0待签到 1进行中 2待签字 3结束 4取消")
    private Integer meetingStatus;
    @ApiModelProperty(value = "会议状态 0待签到 1进行中 2待签字 3结束 4取消")
    private List<Integer> meetingStatusList;
    @ApiModelProperty(value = "参与会议用户id")
    private Long participateMeetingUid;
    @ApiModelProperty(value = "会议参与类型 1参会 2抄送")
    private Integer meetingMemberType;
    @ApiModelProperty(value = "会议参与状态 0待参加 1已签到 2已签字")
    private Integer meetingMemberStatus;
    @ApiModelProperty(value = "会议参与状态 0待参加 1已签到 2已签字")
    private List<Integer> meetingMemberStatusList;
    @ApiModelProperty(value = "超时时间")
    private Date expireTime;
    @ApiModelProperty(value = "会议类型 0普通 1现场")
    private Integer type;

    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;

    @ApiModelProperty(value = "是否开始时间后，如果当前时间是开始时间后，则意味着会议为进行中")
    private String afterStartTime;

}
