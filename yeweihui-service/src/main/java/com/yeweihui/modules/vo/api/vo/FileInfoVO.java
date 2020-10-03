package com.yeweihui.modules.vo.api.vo;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yeweihui.modules.vo.admin.file.FileEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 远程文件信息
 * 
 *
 * @author 朱晓龙
 * 2020年8月28日  上午10:55:38
 */
@Data
@ApiModel(value = "远程文件信息")
public class FileInfoVO {

	@ApiModelProperty(value = "关联ID")
	@Min(value = 1, message="ID必须为大于0的数值")
	private Long id;
	@ApiModelProperty(value = "文件信息")
	@NotNull(message="文件信息不能为null")
	private List<FileEntity> fileInfos;
}
