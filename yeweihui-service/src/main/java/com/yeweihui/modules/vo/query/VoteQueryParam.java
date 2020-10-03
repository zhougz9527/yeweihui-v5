package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "表决查询条件")
public class VoteQueryParam extends BaseQueryParam{
    @ApiModelProperty(value = "小区id")
    private Long zoneId;
    @ApiModelProperty(value = "关键字（经办人手机/经办人）")
    private String keyword;
    @ApiModelProperty(value = "发起用户id")
    private Long uid;
    @ApiModelProperty(value = "参与用户id")
    private Long participateUid;

    @ApiModelProperty(value = "投票状态 1同意过半 2反对过半 3撤销 4选择确定")
    private Integer voteStatus;
    @ApiModelProperty(value = "投票状态 1同意过半 2反对过半 3撤销 4选择确定")
    private List<Integer> voteStatusList;
    @ApiModelProperty(value = "投票参与类型 1表决 2抄送")
    private Integer voteMemberType;
    @ApiModelProperty(value = "投票参与状态 1同意 2反对 3弃权 4超时")
    private Integer voteMemberStatus;


    @ApiModelProperty(value = "投票参与状态列表 1同意 2反对 3弃权 4超时")
    private List<Integer> voteMemberStatusList;

    @ApiModelProperty(value = "获取超时的 true false")
    private String expire;

    @ApiModelProperty(value = "浏览用户id")
    private Long viewUid;
}
