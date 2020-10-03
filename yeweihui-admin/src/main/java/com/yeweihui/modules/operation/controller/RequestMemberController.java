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

import com.yeweihui.modules.operation.entity.RequestMemberEntity;
import com.yeweihui.modules.operation.service.RequestMemberService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 审批成员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/requestmember")
public class RequestMemberController {
    @Autowired
    private RequestMemberService requestMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:requestmember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = requestMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:requestmember:info")
    public R info(@PathVariable("id") Long id){
        RequestMemberEntity requestMember = requestMemberService.selectById(id);

        return R.ok().put("requestMember", requestMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:requestmember:save")
    public R save(@RequestBody RequestMemberEntity requestMember){
        requestMemberService.insert(requestMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:requestmember:update")
    public R update(@RequestBody RequestMemberEntity requestMember){
        ValidatorUtils.validateEntity(requestMember);
        requestMemberService.updateAllColumnById(requestMember);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:requestmember:delete")
    public R delete(@RequestBody Long[] ids){
        requestMemberService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
