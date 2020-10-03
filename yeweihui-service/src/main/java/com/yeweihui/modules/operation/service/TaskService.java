package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.TaskEntity;

import java.util.Map;

/**
 * 工作任务表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface TaskService extends IService<TaskEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

