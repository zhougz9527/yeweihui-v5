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

import com.yeweihui.modules.operation.entity.PaperMemberEntity;
import com.yeweihui.modules.operation.service.PaperMemberService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 函件签收表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("operation/papermember")
public class PaperMemberController {
    @Autowired
    private PaperMemberService paperMemberService;

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("operation:papermember:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = paperMemberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("operation:papermember:info")
    public R info(@PathVariable("id") Integer id){
        PaperMemberEntity paperMember = paperMemberService.selectById(id);

        return R.ok().put("paperMember", paperMember);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("operation:papermember:save")
    public R save(@RequestBody PaperMemberEntity paperMember){
        paperMemberService.insert(paperMember);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("operation:papermember:update")
    public R update(@RequestBody PaperMemberEntity paperMember){
        ValidatorUtils.validateEntity(paperMember);
        paperMemberService.updateAllColumnById(paperMember);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("operation:papermember:delete")
    public R delete(@RequestBody Integer[] ids){
        paperMemberService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
