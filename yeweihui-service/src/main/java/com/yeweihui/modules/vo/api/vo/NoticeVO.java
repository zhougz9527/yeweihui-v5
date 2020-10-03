package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.operation.entity.NoticeEntity;
import com.yeweihui.modules.operation.entity.VoteEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "通知查询条件")
public class NoticeVO extends NoticeEntity {

}
