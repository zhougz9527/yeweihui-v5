package com.yeweihui.modules.vo.api.form;

import com.yeweihui.modules.enums.bill.BillMemberStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BillVerifyForm {
    @ApiModelProperty(value = "费用报销Id")
    private Long bid;
    @ApiModelProperty(value = "用户id")
    private Long uid;
    @ApiModelProperty(value = "状态 0未审批 1已通过 2未通过")
    private BillMemberStatusEnum billMemberStatusEnum;
    @ApiModelProperty(value = "签名url")
    private String verifyUrl;
}
