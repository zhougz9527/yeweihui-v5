package com.yeweihui.modules.vo.api.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.accounts.entity.AccountsSubjectEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@ApiModel(value = "根据科目查询收支")
public class FinancialinformVO extends AccountsFinancialinformEntity{


    @ApiModelProperty(value = "审核人")
    private String auditor;
    @ApiModelProperty(value = "凭证字号")
    private String tagNumber;

    public String getTagNumber() {
        if(tagNumber!=null&&!"".equals(tagNumber)) {
            return "记-"+tagNumber;
        }
        else
        {
            return tagNumber;
        }
    }

    @ApiModelProperty(value = "制单人")
    private String makeUsername;

    @ApiModelProperty(value = "日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @ApiModelProperty(value = "借方")
    @TableField(exist = false)
    private Double debit;
    @ApiModelProperty(value = "贷方")
    @TableField(exist = false)
    private Double credit;
    public Double getDebit() {
        if(super.getAccountsSubject()!=null&&super.getAccountsSubject().getRdtype()!=null)
        {
        if(super.getAccountsSubject().getRdtype()==1)
        {
            return super.getMoney();
        }
        }
        return  null;
    }
    public Double getCredit() {
        if(super.getAccountsSubject()!=null&&super.getAccountsSubject().getRdtype()!=null) {
            if (super.getAccountsSubject().getRdtype() == 2) {
                return super.getMoney();
            }
        }
            return  null;

    }







}
