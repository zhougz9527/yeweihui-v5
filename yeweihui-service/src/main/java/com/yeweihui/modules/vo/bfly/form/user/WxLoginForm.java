package com.yeweihui.modules.vo.bfly.form.user;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation("微信登录信息")
public class WxLoginForm {

    @ApiModelProperty(value = "微信code")
    private String code;

    @ApiModelProperty(value = "rawData")
    private String rawData;

    @ApiModelProperty(value = "signature")
    private String signature;

    @ApiModelProperty(value = "encryptedData")
    private String encryptedData;

    @ApiModelProperty(value = "iv")
    private String iv;

}
