package com.yeweihui.modules.vo.api.form.announce;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "公示记录操作")
public class AnnounceOpeForm {
    @ApiModelProperty(value = "公示id")
    private Long aid;
    @ApiModelProperty(value = "操作人id")
    private Long uid;
    @ApiModelProperty(value = "完成url")
    private String finishUrl;
    @ApiModelProperty(value = "完成文字记录")
    private String remark;
    @ApiModelProperty(value = "操作")
    private String opeName;
    @ApiModelProperty(value = "urls")
    private String urls;

}
