package com.yeweihui.modules.user.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;



/**
 * 业委会用户
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
@RestController
@RequestMapping("user/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("user:user:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        UserEntity user = userService.selectById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("user:user:save")
    public R save(@RequestBody UserEntity user){
        userService.insert(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("user:user:update")
    public R update(@RequestBody UserEntity user){
        ValidatorUtils.validateEntity(user);
        userService.updateAllColumnById(user);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("user:user:delete")
    public R delete(@RequestBody Long[] ids){
        userService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    //小区管理员审核用户
    @RequestMapping("/verifyUser")
    public R verifyUser(@RequestParam("userId") Long userId,
                        @RequestParam("verifyStatus") Integer verifyStatus){
        userService.verifyUser(userId, verifyStatus);

        return R.ok();
    }

    @RequestMapping("/roleUser")
    public R roleUser(@RequestParam("roleName") String roleName) {
        List<UserEntity> userEntities = userService.selectList(
                new EntityWrapper<UserEntity>()
                    .eq(null != roleName, "role_name", roleName)
        );
        return R.ok().put("users", userEntities);
    }


}
