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

import com.yeweihui.modules.operation.entity.TaskMemberEntity;
import com.yeweihui.modules.operation.service.TaskMemberService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 任务人员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/taskmember")
public class TaskMemberController {
    @Autowired
    private TaskMemberService taskMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:taskmember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = taskMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:taskmember:info")
    public R info(@PathVariable("id") Long id){
        TaskMemberEntity taskMember = taskMemberService.selectById(id);

        return R.ok().put("taskMember", taskMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:taskmember:save")
    public R save(@RequestBody TaskMemberEntity taskMember){
        taskMemberService.insert(taskMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:taskmember:update")
    public R update(@RequestBody TaskMemberEntity taskMember){
        ValidatorUtils.validateEntity(taskMember);
        taskMemberService.updateAllColumnById(taskMember);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:taskmember:delete")
    public R delete(@RequestBody Long[] ids){
        taskMemberService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
