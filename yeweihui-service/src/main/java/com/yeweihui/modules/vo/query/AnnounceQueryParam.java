package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "公示记录查询条件")
public class AnnounceQueryParam extends BaseQueryParam {
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "发函人id")
    private Long uid;
    @ApiModelProperty(value = "状态 0正常1撤销")
    private Integer status;
    @ApiModelProperty(value = "是否签收")
    private Integer memberStatus;
    @ApiModelProperty(value = "接收用户id")
    private Long receiverUid;
    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;
    @ApiModelProperty(value = "是否过期")
    private String expire;
    @ApiModelProperty(value = "提前通知时间")
    private Date noticeTime;
    @ApiModelProperty(value = "截止时间")
    private Date endTime;
}
