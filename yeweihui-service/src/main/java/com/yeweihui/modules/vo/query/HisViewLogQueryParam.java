package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "历史记录查询日志查询条件")
public class HisViewLogQueryParam extends BaseQueryParam {

    @ApiModelProperty(value = "小区id")
    private Long zoneId;

    @ApiModelProperty(value = "关键字（经办人）")
    private String keyword;

    @ApiModelProperty(value = "查看人id")
    private Long uid;

    @ApiModelProperty(value = "模块类型 参考BizTypeEnum")
    private Integer type;

}
