package com.yeweihui.modules.vo.query;

import com.yeweihui.modules.enums.UserVerifyStatusEnum;
import com.yeweihui.modules.enums.VerifyStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "用户查询条件")
public class UserQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "角色code列表")
    private List<String> roleCodeList;

    @ApiModelProperty(value = "角色组类型")
    private String groupName;

    @ApiModelProperty(value = "用户审核状态 0审核中 1审核通过 2撤销 3未通过")
    private Integer userVerifyStatus;

    @ApiModelProperty(value = "状态 0禁用 1正常")
    private Integer status;

}
