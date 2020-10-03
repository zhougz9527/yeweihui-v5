package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.TaskLogEntity;

import java.util.Map;

/**
 * 任务完成日志表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface TaskLogService extends IService<TaskLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

