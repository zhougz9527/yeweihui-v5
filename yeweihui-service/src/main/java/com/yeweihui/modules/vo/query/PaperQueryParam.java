package com.yeweihui.modules.vo.query;

import com.yeweihui.modules.enums.paper.PaperStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
@ApiModel(value = "函件查询条件")
public class PaperQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人）")
    private String keyword;

    @ApiModelProperty(value = "发函人id")
    private Long uid;
    @ApiModelProperty(value = "函件状态 状态 0未签收 1已签收")
    private Integer paperStatus;

    @ApiModelProperty(value = "是否签收")
    private Integer paperMemberStatus;
    @ApiModelProperty(value = "收函用户id")
    private Long receiverUid;

    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;

    @ApiModelProperty(value = "记录状态 0删除 1业主不显示 2通过")
    private transient Integer minRecordStatus;
}
