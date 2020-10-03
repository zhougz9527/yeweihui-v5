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

import com.yeweihui.modules.operation.entity.VoteMemberEntity;
import com.yeweihui.modules.operation.service.VoteMemberService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 表决成员表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/votemember")
public class VoteMemberController {
    @Autowired
    private VoteMemberService voteMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:votemember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = voteMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:votemember:info")
    public R info(@PathVariable("id") Long id){
        VoteMemberEntity voteMember = voteMemberService.selectById(id);

        return R.ok().put("voteMember", voteMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:votemember:save")
    public R save(@RequestBody VoteMemberEntity voteMember){
        voteMemberService.insert(voteMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:votemember:update")
    public R update(@RequestBody VoteMemberEntity voteMember){
        ValidatorUtils.validateEntity(voteMember);
        voteMemberService.updateAllColumnById(voteMember);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:votemember:delete")
    public R delete(@RequestBody Long[] ids){
        voteMemberService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
