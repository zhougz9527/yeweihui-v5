package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.operation.entity.RequestEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用章vo")
public class RequestVO extends RequestEntity {
    @ApiModelProperty(value = "小区名称")
    private String zoneName;
    @ApiModelProperty(value = "发起用户头像")
    private String avatarUrl;
}
