package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "用章查询条件")
public class RequestQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人）")
    private String keyword;

    @ApiModelProperty(value = "类型 1审批 2抄送")
    private Integer requestMemberType;
    @ApiModelProperty(value = "用章状态 0未审核 1通过 2未通过")
    private Integer requestMemberStatus;
    @ApiModelProperty(value = "发起人uid")
    private Long uid;
    @ApiModelProperty(value = "审批人/抄送人uid")
    private Long verifyUid;

    @ApiModelProperty(value = "状态 0审核中 1审核通过 2撤销 3未通过")
    private List<Integer> requestStatusList;

    @ApiModelProperty(value = "用章状态列表 0未审核 1通过 2未通过")
    private List<Integer> requestMemberStatusList;

    @ApiModelProperty(value = "超时时间，24小时前")
    private Date expireTime;
    @ApiModelProperty(value = "超时时间，24小时前2")
    private Date maxExpireTime;
    @ApiModelProperty(value = "状态 0审核中 1审核通过 2撤销 3未通过")
    private Integer requestStatus;
    @ApiModelProperty(value = "截止时间")
    private Date endTime;

    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;
}