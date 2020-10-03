package com.yeweihui.modules.vo.bfly.form.user;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiOperation(value = "提交审核表单")
public class SubmitCertFrom {

    @ApiModelProperty(value = "用户id")
    private Long bflyUserId;

    @ApiModelProperty(value = "房屋的id")
    private Long bflyRoomId;

    @NotNull(message="小区id不能为空")
    @ApiModelProperty(value = "小区id")
    private Long zoneId;

    @NotBlank(message="小区名称不能为空")
    @ApiModelProperty(value = "小区名称")
    private String zoneName;

    @ApiModelProperty(value = "小区苑")
    private String court;

    @ApiModelProperty(value = "小区幢")
    private String building;

    @ApiModelProperty(value = "小区单元")
    private String unitName;

    @NotBlank(message="小区楼号不能为空")
    @ApiModelProperty(value = "小区楼号")
    private String floorName;

    @NotBlank(message="提交审核的电话号码不能为空")
    @ApiModelProperty(value = "提交审核的电话号码")
    private String checkPhoneNum;

    @NotBlank(message="提交审核的身份证号码不能为空")
    @ApiModelProperty(value = "提交审核的身份证号码")
    private String checkIdCard;

    @NotBlank(message="提交审核的业主姓名不能为空")
    @ApiModelProperty(value = "提交审核的业主姓名")
    private String checkUserName;

//    @NotNull(message="提交审核的建筑面积不能为空")
    @ApiModelProperty(value = "提交审核的建筑面积")
    private BigDecimal checkHousingArea;

//    @NotBlank(message="提交审核的房产证图片url不能为空")
    @ApiModelProperty(value = "提交审核的房产证图片url")
    private String checkHouseCertificateUrl;

//    @NotBlank(message="提交审核的身份证图片url不能为空")
    @ApiModelProperty(value = "提交审核的身份证图片url")
    private String checkIdCardUrl;

//    @NotBlank(message="提交审核的头像url不能为空")
    @ApiModelProperty(value = "提交审核的头像url")
    private String checkHeaderUrl;

    @ApiModelProperty(value = "审核的状态")
    private Integer status;

    /**
     * 是否提交管理员手动验证
     */
    @ApiModelProperty(value = "是否提交管理员手动验证")
    private Boolean pass;

}
