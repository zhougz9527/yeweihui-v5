package com.yeweihui.modules.division;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.division.entity.DivisionManagerEntity;
import com.yeweihui.modules.division.service.DivisionManagerService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("division/manager")
public class DivisionManageController {

    @Autowired
    DivisionManagerService divisionManagerService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("division:divisionManager:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = divisionManagerService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("division:divisionManager:info")
    public R info(@PathVariable("id") Long id){
        DivisionManagerEntity divisionManager = divisionManagerService.selectById(id);

        return R.ok().put("divisionManager", divisionManager);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("division:divisionManager:save")
    public R save(@RequestBody DivisionManagerEntity divisionManager){
        //校验类型
        ValidatorUtils.validateEntity(divisionManager);

        divisionManagerService.insert(divisionManager);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("division:divisionManager:update")
    public R update(@RequestBody DivisionManagerEntity divisionManager){
        //校验类型
        ValidatorUtils.validateEntity(divisionManager);

        divisionManagerService.updateById(divisionManager);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("division:divisionManager:delete")
    public R delete(@RequestBody Long[] ids){
        divisionManagerService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }
}
