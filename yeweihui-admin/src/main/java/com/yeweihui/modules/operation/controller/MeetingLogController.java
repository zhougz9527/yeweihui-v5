package com.yeweihui.modules.operation.controller;

import java.util.Arrays;
import java.util.Map;

import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.operation.entity.MeetingLogEntity;
import com.yeweihui.modules.operation.service.MeetingLogService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 会议记录表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/meetinglog")
public class MeetingLogController {
    @Autowired
    private MeetingLogService meetingLogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:meetinglog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = meetingLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:meetinglog:info")
    public R info(@PathVariable("id") Long id){
        MeetingLogEntity meetingLog = meetingLogService.info(id);

        return R.ok().put("meetingLog", meetingLog);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:meetinglog:save")
    public R save(@RequestBody MeetingLogEntity meetingLog){
        meetingLog.setUid(ShiroUtils.getUserId());
        meetingLogService.save(meetingLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:meetinglog:update")
    public R update(@RequestBody MeetingLogEntity meetingLog){
        ValidatorUtils.validateEntity(meetingLog);
        meetingLogService.updateAllColumnById(meetingLog);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:meetinglog:delete")
    public R delete(@RequestBody Long[] ids){
        meetingLogService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
