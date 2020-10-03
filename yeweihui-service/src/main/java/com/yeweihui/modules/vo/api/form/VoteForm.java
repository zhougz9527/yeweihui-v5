package com.yeweihui.modules.vo.api.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "表决选项选择")
public class VoteForm {
    @ApiModelProperty(value = "表决id")
    private Long vid;
    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "投票状态 0未选择 1同意 2反对 3弃权 4超时")
    private Integer status;
    @ApiModelProperty(value = "多选 选项1 选择了置为1")
    private Integer item1;
    @ApiModelProperty(value = "多选 选项2")
    private Integer item2;
    @ApiModelProperty(value = "多选 选项3")
    private Integer item3;
    @ApiModelProperty(value = "多选 选项4")
    private Integer item4;
    @ApiModelProperty(value = "发表意见")
    private String remark;
    @ApiModelProperty(value = "签名url")
    private String verifyUrl;
}