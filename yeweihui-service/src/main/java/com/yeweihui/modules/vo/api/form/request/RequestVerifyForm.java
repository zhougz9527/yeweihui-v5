package com.yeweihui.modules.vo.api.form.request;

import com.yeweihui.modules.enums.request.RequestMemberVerifyStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用章审核通过拒绝请求")
public class RequestVerifyForm {
    @ApiModelProperty(value = "用章Id")
    private Long requestId;
    @ApiModelProperty(value = "用户id")
    private Long uid;
    @ApiModelProperty(value = "审批状态（通过/未通过）1")
    private RequestMemberVerifyStatusEnum requestMemberVerifyStatusEnum;
    @ApiModelProperty(value = "审批签名url")
    private String verifyUrl;
}