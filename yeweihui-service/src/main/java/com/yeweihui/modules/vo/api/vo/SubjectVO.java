package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "根据科目查询收支")
public class SubjectVO  extends AccountsSubjectEntity{
    @ApiModelProperty(value = "金额")
    private String money;

    @ApiModelProperty(value = "一级科目名称")

    private String pname;
    @ApiModelProperty(value = "二级科目名称")

    private String sname;

    @ApiModelProperty(value = "下级科目数量")
    private Integer scount;


}
