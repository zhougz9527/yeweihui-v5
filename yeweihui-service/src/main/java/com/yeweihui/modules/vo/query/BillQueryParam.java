package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "报销查询条件")
public class BillQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人）")
    private String keyword;

    @ApiModelProperty(value = "报销用户id")
    private Long uid;
    @ApiModelProperty(value = "报销状态 0等待 1通过 2未通过")
    private Integer billStatus;
    @ApiModelProperty(value = "报销类型 1小额主任审批 2大额业委会审批")
    private Integer billType;
    @ApiModelProperty(value = "报销参与状态 0未审批 1已通过 2未通过")
    private Integer billMemberStatus;
    @ApiModelProperty(value = "审批用户id")
    private Long verifyUid;

    @ApiModelProperty(value = "报销状态 0等待 1通过 2未通过")
    private List<Integer> billStatusList;

    @ApiModelProperty(value = "报销状态列表 0等待 1通过 2未通过")
    private List<Integer> billMemberStatusList;

    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;
}
