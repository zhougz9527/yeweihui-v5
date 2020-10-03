package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.TaskMemberEntity;

import java.util.Map;

/**
 * 任务人员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface TaskMemberService extends IService<TaskMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

