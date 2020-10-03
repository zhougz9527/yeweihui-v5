package com.yeweihui.modules.division;

import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.division.entity.CommunityEntity;
import com.yeweihui.modules.division.service.CommunityService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("division/community")
public class CommunityController {

    @Autowired
    CommunityService communityService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("division:community:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = communityService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 所有社区返回，供关联用
     */
    @RequestMapping("/all")
    @RequiresPermissions("division:community:list")
    public R all() {
        List<CommunityEntity> all = communityService.selectByMap(new HashMap<>());
        return R.ok().put("all", all);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("division:community:info")
    public R info(@PathVariable("id") Long id){
        CommunityEntity community = communityService.selectById(id);

        return R.ok().put("community", community);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("division:community:save")
    public R save(@RequestBody CommunityEntity community){
        //校验类型
        ValidatorUtils.validateEntity(community);

        communityService.insert(community);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("division:community:update")
    public R update(@RequestBody CommunityEntity community){
        //校验类型
        ValidatorUtils.validateEntity(community);

        communityService.updateById(community);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("division:community:delete")
    public R delete(@RequestBody Long[] ids){
        communityService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据街道id获取所有社区
     */
    @RequestMapping("/communitiesInSubdistrict")
    @RequiresPermissions("division:community:list")
    public R communityBySubdistrict(@RequestParam("subdistrictId") Long subdistrictId) {
        List<CommunityEntity> communityEntityList = communityService.findBySubdistrictId(subdistrictId);
        return R.ok().put("communities", communityEntityList);
    }
}
