package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务查询条件")
public class TaskQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人）")
    private String keyword;

}
