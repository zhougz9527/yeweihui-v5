package com.yeweihui.modules.vo.query;

/**
 * Created by zss86 on 2020/8/24.
 */
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@ApiModel(value = "收支结余信息查询条件")
public class ReceiptsAndDisbursementsParam extends BaseQueryParam{

    @ApiModelProperty(value = "账簿ID")
    private Long id;
    @ApiModelProperty(value = "账簿所属月份日期，格式：yyyy-MM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM")
    @DateTimeFormat(pattern="yyyy-MM")
    private Date accountsDate;
    @ApiModelProperty(value = "按年统计")
    private Integer yearDate;
    @ApiModelProperty(value = "小区ID - 绑定小区信息")
    private Long zoneId;
    @ApiModelProperty(value = "账簿状态:0(录入、默认)、1(审核中)、2(审核失败)、3(审核成功)")
    private Integer status;
    @ApiModelProperty(value = "科目类型：1(经营)、2(押金)")
    private Integer type;
    @ApiModelProperty(value = "收支类型：1(收入)、2(支出),收支(receipts and disbursements)")
    private Integer rdtype;
    @ApiModelProperty(value = "科目ID")
    private Integer subjectId;

}

