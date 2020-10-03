package com.yeweihui.modules.user.controller;

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

import com.yeweihui.modules.user.entity.DuqinAppUserEntity;
import com.yeweihui.modules.user.service.DuqinAppUserService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * APP用户
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-04-01 17:30:16
 */
@RestController
@RequestMapping("user/duqinappuser")
public class DuqinAppUserController {
    @Autowired
    private DuqinAppUserService duqinAppUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:duqinappuser:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = duqinAppUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:duqinappuser:info")
    public R info(@PathVariable("id") Long id){
        DuqinAppUserEntity duqinAppUser = duqinAppUserService.selectById(id);

        return R.ok().put("duqinAppUser", duqinAppUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:duqinappuser:save")
    public R save(@RequestBody DuqinAppUserEntity duqinAppUser){
        duqinAppUserService.insert(duqinAppUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:duqinappuser:update")
    public R update(@RequestBody DuqinAppUserEntity duqinAppUser){
        ValidatorUtils.validateEntity(duqinAppUser);
        duqinAppUserService.updateAllColumnById(duqinAppUser);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:duqinappuser:delete")
    public R delete(@RequestBody Long[] ids){
        duqinAppUserService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
