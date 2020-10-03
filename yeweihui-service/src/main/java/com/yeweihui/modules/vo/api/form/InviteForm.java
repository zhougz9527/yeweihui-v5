package com.yeweihui.modules.vo.api.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "邀请用户表单")
public class InviteForm {
    /*@NotBlank(message="用户手机号不能为空")
    @ApiModelProperty(value = "用户手机号")
    private String phone;*/

    @NotBlank(message="openId不能为空")
    @ApiModelProperty(value = "openId")
    private String openId;
    @NotBlank(message="邀请码不能为空")
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
    @NotNull(message="用户申请的角色id不能为空")
    @ApiModelProperty(value = "用户申请的角色id")
    private Long roleId;
    @ApiModelProperty(value = "房间号")
    private String roomNum;

    /*@ApiModelProperty(value = "验证码")
    private String verifyCode;*/
    @ApiModelProperty(value = "真实姓名")
    private String realname;
}
