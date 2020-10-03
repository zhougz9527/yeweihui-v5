package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "小区查询条件")
public class ZonesQueryParam extends BaseQueryParam {
    @ApiModelProperty(value = "关键字（小区名称/邀请码）")
    private String keyword;
    @ApiModelProperty(value = "小区名称")
    private String zoneName;
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
}
