package com.yeweihui.modules.operation.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.TaskLogDao;
import com.yeweihui.modules.operation.entity.TaskLogEntity;
import com.yeweihui.modules.operation.service.TaskLogService;


@Service("taskLogService")
public class TaskLogServiceImpl extends ServiceImpl<TaskLogDao, TaskLogEntity> implements TaskLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TaskLogEntity> page = this.selectPage(
                new Query<TaskLogEntity>(params).getPage(),
                new EntityWrapper<TaskLogEntity>()
        );

        return new PageUtils(page);
    }

}
