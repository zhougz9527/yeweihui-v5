package com.yeweihui.modules.vo.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AnnounceMemberQueryParam {
    @ApiModelProperty(value = "公示记录id")
    private Long aid;
    @ApiModelProperty(value = "状态 0未读1已读")
    private Integer memberStatus;
    @ApiModelProperty(value = "浏览人id")
    private Long viewUid;
}
