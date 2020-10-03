package com.yeweihui.modules.operation.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.operation.dao.TaskMemberDao;
import com.yeweihui.modules.operation.entity.TaskMemberEntity;
import com.yeweihui.modules.operation.service.TaskMemberService;


@Service("taskMemberService")
public class TaskMemberServiceImpl extends ServiceImpl<TaskMemberDao, TaskMemberEntity> implements TaskMemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TaskMemberEntity> page = this.selectPage(
                new Query<TaskMemberEntity>(params).getPage(),
                new EntityWrapper<TaskMemberEntity>()
        );

        return new PageUtils(page);
    }

}
