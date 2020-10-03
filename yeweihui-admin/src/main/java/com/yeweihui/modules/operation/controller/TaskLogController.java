package com.yeweihui.modules.operation.controller;

import java.util.Arrays;
import java.util.Map;

import com.yeweihui.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.entity.TaskLogEntity;
import com.yeweihui.modules.operation.service.TaskLogService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 任务完成日志表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/tasklog")
public class TaskLogController {
    @Autowired
    private TaskLogService taskLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:tasklog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = taskLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:tasklog:info")
    public R info(@PathVariable("id") Long id){
        TaskLogEntity taskLog = taskLogService.selectById(id);

        return R.ok().put("taskLog", taskLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:tasklog:save")
    public R save(@RequestBody TaskLogEntity taskLog){
        taskLogService.insert(taskLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:tasklog:update")
    public R update(@RequestBody TaskLogEntity taskLog){
        ValidatorUtils.validateEntity(taskLog);
        taskLogService.updateAllColumnById(taskLog);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:tasklog:delete")
    public R delete(@RequestBody Long[] ids){
        taskLogService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
