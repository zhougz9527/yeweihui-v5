package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.user.entity.ZonesEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "首页头部信息1")
public class MainPage1VO {
    @ApiModelProperty(value = "小区信息")
    private ZonesEntity zonesEntity;
    @ApiModelProperty(value = "待我用章审批数量")
    private Integer waitRequestVerifyCount;
    @ApiModelProperty(value = "待我事项表决数量")
    private Integer waitVoteVerifyCount;
    @ApiModelProperty(value = "网上发函数量")
    private Integer waitPaperVerifyCount;
    @ApiModelProperty(value = "费用报销数量")
    private Integer waitBillVerifyCount;
    @ApiModelProperty(value = "未开的但是我参加的会议数量")
    private Integer waitJoinMeetingCount;
    @ApiModelProperty(value = "我未读的通知数量")
    private Integer unreadNoticeCount;
    @ApiModelProperty(value = "即将完成的公示")
    private Integer noticedAnnounce;
    @ApiModelProperty(value = "所有未处理")
    private Integer totalUndo;
}
