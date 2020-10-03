package com.yeweihui.modules.vo.api.form.paper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "签收发函")
public class PaperSignForm {
    @ApiModelProperty(value = "函件id")
    private Long pid;
    @ApiModelProperty(value = "发函签收用户id")
    private Long uid;
    @ApiModelProperty(value = "签名url")
    private String verifyUrl;

}
