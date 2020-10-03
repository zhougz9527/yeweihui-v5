package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.TaskEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.operation.service.TaskService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.TaskQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 工作任务表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/task")
@Api(tags="工作任务")
public class ApiTaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private HisViewLogService hisViewLogService;

    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:task:list")
    public R list(TaskQueryParam taskQueryParam){
        PageUtils page = taskService.queryPage(BeanUtil.bean2map(taskQueryParam));

        return R.ok().put("page", page);
    }

    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    @RequiresPermissions("operation:task:info")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        TaskEntity task = taskService.selectById(id);

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.TASK, userEntity, id);
        }

        return R.ok().put("task", task);
    }

    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:task:save")
    public R save(@RequestBody TaskEntity task){
        taskService.insert(task);

        return R.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:task:update")
    public R update(@RequestBody TaskEntity task){
        ValidatorUtils.validateEntity(task);
        taskService.updateAllColumnById(task);//全部更新
        
        return R.ok();
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
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

}
