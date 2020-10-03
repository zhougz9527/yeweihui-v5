package com.yeweihui.modules.vo.api.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
@ApiModel(value = "更新用户表单")
public class UpdateUserForm {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "微信头像Url")
    @NotBlank(message="头像不能为空")
    private String avatarUrl;

}
