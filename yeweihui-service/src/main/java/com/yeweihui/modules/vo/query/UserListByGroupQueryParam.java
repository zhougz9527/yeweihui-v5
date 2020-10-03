package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "根据小区，用户组获取用户列表, groupName:业主委员会 查询条件")
public class UserListByGroupQueryParam {
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "角色组类型")
    private String groupName;
}
