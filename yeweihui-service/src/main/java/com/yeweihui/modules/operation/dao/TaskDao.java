package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.operation.entity.TaskEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.api.vo.TaskVO;

import java.util.List;
import java.util.Map;

/**
 * 工作任务表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface TaskDao extends BaseMapper<TaskEntity> {

    List<TaskVO> selectPageVO(Page page, Map<String, Object> params);
}
