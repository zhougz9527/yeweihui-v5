package com.yeweihui.modules.sys.controller;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.sys.entity.DuqinUserEntity;
import com.yeweihui.modules.sys.service.DuqinUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 用户表（dq_user） 20180911
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-10-21 22:37:17
 */
@RestController
@RequestMapping("sys/yeweihuiuser")
@Api(value = "sys/yeweihuiuser", description = "api-用户")
public class DuqinUserController {
    @Autowired
    private DuqinUserService duqinUserService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiresPermissions("sys:yeweihuiuser:list")
    @ApiOperation(value = "用户-分页查询")
    @ApiResponses({@ApiResponse(code = 200, message = "返回结果", response = DuqinUserEntity.class)})
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = duqinUserService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("sys:yeweihuiuser:info")
    public R info(@PathVariable("id") Long id){
        DuqinUserEntity yeweihuiUser = duqinUserService.selectById(id);

        return R.ok().put("yeweihuiUser", yeweihuiUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:yeweihuiuser:save")
    public R save(@RequestBody DuqinUserEntity duqinUser){
        duqinUserService.insert(duqinUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:duqinuser:update")
    public R update(@RequestBody DuqinUserEntity duqinUser){
        ValidatorUtils.validateEntity(duqinUser);
        duqinUserService.updateAllColumnById(duqinUser);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:duqinuser:delete")
    public R delete(@RequestBody Long[] ids){
        duqinUserService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
