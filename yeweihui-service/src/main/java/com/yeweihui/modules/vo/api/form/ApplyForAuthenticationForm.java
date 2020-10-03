package com.yeweihui.modules.vo.api.form;

import com.yeweihui.modules.enums.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ApiModel(value = "申请认证（业委会达人，业委会歌手，业委会号）")
public class ApplyForAuthenticationForm {
    @ApiModelProperty(value = "app用户id")
    private Long userId;

    @ApiModelProperty(value = "自我介绍")
    @NotBlank(message="自我介绍不能为空")
    private String description;

    @ApiModelProperty(value = "身份证正面照片")
    @NotBlank(message="身份证正面照片不能为空")
    private String idCardPictureUrl;

    @ApiModelProperty(value = "申请的用户类型")
    private UserTypeEnum applyForType;
}
