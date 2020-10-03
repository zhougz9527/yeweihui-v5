package com.yeweihui.modules.vo.bfly.form.zone;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "小区结构")
public class ZoneStructureForm {

    @ApiModelProperty(value = "小区名称")
    private String zoneName;
    @ApiModelProperty(value = "小区苑")
    private String court;
    @ApiModelProperty(value = "小区幢")
    private String building;

}
