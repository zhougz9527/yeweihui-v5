package com.yeweihui.modules.vo.bfly.form.user;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiOperation(value = "用户信息")
public class UserForm {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "小区的id")
    private Long zoneId;

    @ApiModelProperty(value = "房屋的id")
    private Long bflyRoomId;

    @ApiModelProperty(value = "电话号码")
    private String phoneNum;

    @ApiModelProperty(value = "身份证号码")
    private String idCard;

    @ApiModelProperty(value = "业主姓名")
    private String userName;

    @ApiModelProperty(value = "建筑面积")
    private BigDecimal housingArea;

    @ApiModelProperty(value = "提交审核的电话号码")
    private String checkPhoneNum;

    @ApiModelProperty(value = "提交审核的身份证号码")
    private String checkIdCard;

    @ApiModelProperty(value = "提交审核的业主姓名")
    private String cehckUserName;

    @ApiModelProperty(value = "提交审核的建筑面积")
    private BigDecimal checkHousingArea;

    @ApiModelProperty(value = "房产证图片url")
    private String houseCertificateUrl;

    @ApiModelProperty(value = "身份证图片url")
    private String idCardUrl;

    @ApiModelProperty(value = "头像url")
    private String headerUrl;

    @ApiModelProperty(value = "提交审核的房产证图片url")
    private String checkHouseCertificateUrl;

    @ApiModelProperty(value = "提交审核的身份证图片url")
    private String checkIdCardUrl;

    @ApiModelProperty(value = "提交审核的头像url")
    private String checkHeaderUrl;

}
