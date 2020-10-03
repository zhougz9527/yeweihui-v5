package com.yeweihui.modules.vo.api.form.meeting;

import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "上传会议纪要")
public class MeetingLogUploadForm extends MeetingLogEntity {
    @ApiModelProperty(value = "会议id")
    private Long mid;
    @ApiModelProperty(value = "上传会议纪要用户id")
    private Long uid;
}
