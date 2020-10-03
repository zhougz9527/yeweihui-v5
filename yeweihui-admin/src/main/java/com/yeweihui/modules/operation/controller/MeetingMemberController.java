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

import com.yeweihui.modules.operation.entity.MeetingMemberEntity;
import com.yeweihui.modules.operation.service.MeetingMemberService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 参会人员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/meetingmember")
public class MeetingMemberController {
    @Autowired
    private MeetingMemberService meetingMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:meetingmember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = meetingMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:meetingmember:info")
    public R info(@PathVariable("id") Long id){
        MeetingMemberEntity meetingMember = meetingMemberService.selectById(id);

        return R.ok().put("meetingMember", meetingMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:meetingmember:save")
    public R save(@RequestBody MeetingMemberEntity meetingMember){
        meetingMemberService.insert(meetingMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:meetingmember:update")
    public R update(@RequestBody MeetingMemberEntity meetingMember){
        ValidatorUtils.validateEntity(meetingMember);
        meetingMemberService.updateAllColumnById(meetingMember);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:meetingmember:delete")
    public R delete(@RequestBody Long[] ids){
        meetingMemberService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
