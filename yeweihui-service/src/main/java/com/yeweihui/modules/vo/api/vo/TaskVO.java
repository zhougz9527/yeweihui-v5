package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.operation.entity.TaskEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "任务vo")
public class TaskVO extends TaskEntity {
    @ApiModelProperty(value = "小区名称")
    private String zoneName;
}
