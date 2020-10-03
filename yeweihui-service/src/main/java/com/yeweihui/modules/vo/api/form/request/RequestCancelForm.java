package com.yeweihui.modules.vo.api.form.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用章取消表单")
public class RequestCancelForm {
    @ApiModelProperty(value = "投票id")
    private Long rid;
    @ApiModelProperty(value = "发起人id")
    private Long uid;
}
