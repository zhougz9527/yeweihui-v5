package com.yeweihui.modules.vo.bfly.form.room;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "校验房屋信息")
public class CheckRoomForm {
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "苑")
    private String court;
    @ApiModelProperty(value = "楼号")
    private String building;
    @ApiModelProperty(value = "单元")
    private String unitName;
    @ApiModelProperty(value = "房号")
    private String floorName;
    @ApiModelProperty(value = "手机号")
    private String phoneNum;

}
