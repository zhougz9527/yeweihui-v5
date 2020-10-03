package com.yeweihui.modules.vo.query;

import lombok.Data;

@Data
public class PaperMemberQueryParam {
    private Long pid;
    //是否签收 0未签收 1已签收
    private Integer paperMemberStatus;
}
