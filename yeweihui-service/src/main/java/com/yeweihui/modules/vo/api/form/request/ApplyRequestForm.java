package com.yeweihui.modules.vo.api.form.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeweihui.modules.enums.UserTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "申请用章")
public class ApplyRequestForm {

    @ApiModelProperty(value = "申请人id")
    private Long uid;

    @ApiModelProperty(value = "使用日期")
//	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date useDate;

    @ApiModelProperty(value = "文件名")
    private String documentName;

    @ApiModelProperty(value = "文件份数")
    private Integer num;

    @ApiModelProperty(value = "类别 1合同 2公告 3发函 4其他")
    private Integer fileType;

    @ApiModelProperty(value = "印章 1业主大会 2业委会 3财务专用章 4其他")
    private Integer seal;

    @ApiModelProperty(value = "备注")
    private String notice;

    @ApiModelProperty(value = "审批人id列表")
    List<Long> verifyUserIdList;

    @ApiModelProperty(value = "抄送人id列表")
    List<Long> copyToUserIdList;
}
