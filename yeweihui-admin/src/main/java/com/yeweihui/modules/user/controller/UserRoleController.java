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

import com.yeweihui.modules.user.entity.UserRoleEntity;
import com.yeweihui.modules.user.service.UserRoleService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 用户角色表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
@RestController
@RequestMapping("user/userrole")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:userrole:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userRoleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("user:userrole:info")
    public R info(@PathVariable("id") Integer id){
        UserRoleEntity userRole = userRoleService.selectById(id);

        return R.ok().put("userRole", userRole);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:userrole:save")
    public R save(@RequestBody UserRoleEntity userRole){
        userRoleService.insert(userRole);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:userrole:update")
    public R update(@RequestBody UserRoleEntity userRole){
        ValidatorUtils.validateEntity(userRole);
        userRoleService.updateAllColumnById(userRole);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:userrole:delete")
    public R delete(@RequestBody Integer[] ids){
        userRoleService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
