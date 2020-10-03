package com.yeweihui.modules.vo.api.form.vote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "投票取消表单")
public class VoteCancelForm {
    @ApiModelProperty(value = "投票id")
    private Long vid;
    @ApiModelProperty(value = "发起人id")
    private Long uid;
}
