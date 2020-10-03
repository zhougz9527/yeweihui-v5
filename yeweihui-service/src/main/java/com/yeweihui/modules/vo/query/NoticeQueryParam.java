package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "通知查询条件")
public class NoticeQueryParam extends BaseQueryParam {
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人手机/经办人）")
    private String keyword;
    @ApiModelProperty(value = "发起用户id")
    private Long uid;
    @ApiModelProperty(value = "阅读用户id")
    private Long readUid;
    @ApiModelProperty(value = "通知已读状态 1未读 2已读")
    private Integer noticeMemberStatus;
    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;
    @ApiModelProperty(value = "是否需要全小区，行业主管用")
    private Boolean whole;
}
