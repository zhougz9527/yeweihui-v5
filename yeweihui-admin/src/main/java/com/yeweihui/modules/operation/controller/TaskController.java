package com.yeweihui.modules.operation.controller;

import java.util.ArrayList;
import java.util.List;

import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.operation.entity.TaskEntity;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.vo.query.TaskQueryParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.service.TaskService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 工作任务表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:task:list")
    public R list(TaskQueryParam taskQueryParam){
        PageUtils page = taskService.queryPage(BeanUtil.bean2map(taskQueryParam));

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:task:info")
    public R info(@PathVariable("id") Long id){
        TaskEntity task = taskService.selectById(id);

        return R.ok().put("task", task);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:task:save")
    public R save(@RequestBody TaskEntity task){
        taskService.insert(task);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:task:update")
    public R update(@RequestBody TaskEntity task){
        ValidatorUtils.validateEntity(task);
        taskService.updateAllColumnById(task);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("operation:task:delete")
    public R delete(@RequestBody Long[] ids){
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (Long id: ids) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId(id);
            taskEntity.setRecordStatus(0);
            taskEntities.add(taskEntity);
        }
        taskService.insertOrUpdateBatch(taskEntities);

        return R.ok();
    }

    /**
     * 隐藏
     */
    @RequestMapping("/hide")
    @RequiresPermissions("operation:task:hide")
    public R hide(@RequestBody Long[] ids){
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (Long id: ids) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId(id);
            taskEntity.setRecordStatus(1);
            taskEntities.add(taskEntity);
        }
        taskService.insertOrUpdateBatch(taskEntities);

        return R.ok();
    }

    /**
     * 恢复
     */
    @RequestMapping("/recovery")
    @RequiresPermissions("operation:task:recovery")
    public R recovery(@RequestBody Long[] ids){
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (Long id: ids) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId(id);
            taskEntity.setRecordStatus(2);
            taskEntities.add(taskEntity);
        }
        taskService.insertOrUpdateBatch(taskEntities);

        return R.ok();
    }


}
