package com.yeweihui.modules.operation.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.operation.dao.TaskDao;
import com.yeweihui.modules.operation.entity.TaskEntity;
import com.yeweihui.modules.operation.service.TaskService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.vo.api.vo.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskDao, TaskEntity> implements TaskService {

    @Autowired
    private UserService userService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /*Page<TaskEntity> page = this.selectPage(
                new Query<TaskEntity>(params).getPage(),
                new EntityWrapper<TaskEntity>()
        );*/
        Page page = new Query(params).getPage();
        Long viewUid = (Long) params.get("viewUid");
        if (viewUid == null) {
            viewUid = ShiroUtils.getUserId();
        }
        int minRecordStatus = 2;
        if (viewUid == Constant.SUPER_ADMIN) {
            minRecordStatus = 0;
        }
        UserEntity user = userService.selectById(viewUid);
        String roleCode = user.getRoleCode();
        if (roleCode.equals("1000") || roleCode.startsWith("13")) {
            minRecordStatus = 1;
        }
        params.put("minRecordStatus", minRecordStatus);
        List<TaskVO> voList = this.baseMapper.selectPageVO(page, params);
        page.setRecords(voList);

        return new PageUtils(page);
    }

}
