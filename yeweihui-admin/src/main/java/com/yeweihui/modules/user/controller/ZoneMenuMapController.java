package com.yeweihui.modules.user.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.operation.entity.MenuMapEntity;
import com.yeweihui.modules.sys.controller.AbstractController;
import com.yeweihui.modules.sys.service.MenuMapService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZoneMenuMapEntity;
import com.yeweihui.modules.user.service.ZoneGroupService;
import com.yeweihui.modules.user.service.ZoneMenuMapService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 小区组管理
 */
@RestController
@RequestMapping("user/zoneMenuMap")
public class ZoneMenuMapController extends AbstractController {

    @Autowired
    private ZoneGroupService zoneGroupService;

    @Autowired
    private MenuMapService menuMapService;

    @Autowired
    private ZoneMenuMapService zoneMenuMapService;

    @GetMapping("/zoneGroups")
    @RequiresPermissions("operation:menusetting:list")
    public R zoneGroups() {
        return R.ok().put("zoneGroups", zoneGroupService.selectList(null));
    }

    @GetMapping("/menuMaps")
    @RequiresPermissions("operation:menusetting:info")
    public R menuMaps() {
        List<MenuMapEntity> menuMaps = menuMapService.selectList(new EntityWrapper<MenuMapEntity>().eq("is_hide", 0));
        menuMaps = menuMaps.stream().peek(menuMap -> menuMap.setName(menuMap.getFirstLevel()
                + " " + menuMap.getSecondLevel() + " " + menuMap.getModuleName())).collect(Collectors.toList());
        return R.ok().put("menuMaps", menuMaps);
    }

    @GetMapping("/queryByZoneId")
    @RequiresPermissions("operation:menusetting:info")
    public R queryByZoneId(Long zoneId, Long groupId) {
        UserEntity user = getUser();
        if (user.getId() != Constant.SUPER_ADMIN){
            zoneId = user.getZoneId();
        }
        return R.ok().put("zoneMenuMaps", zoneMenuMapService.selectListByZoneIdAndGroupId(zoneId, groupId));
    }

    @PostMapping("/updateByZoneMenuMap")
    @RequiresPermissions("operation:menusetting:update")
    public R updateByZoneMenuMap(@RequestBody ZoneMenuMapEntity zoneMenuMapEntity) {
        UserEntity user = getUser();
        if (user.getId() == Constant.SUPER_ADMIN || user.getZoneId().equals(zoneMenuMapEntity.getZoneId())){
            return R.ok().put("zoneMenuMap", zoneMenuMapService.insertOrUpdate(zoneMenuMapEntity));
        }
        return R.error();
    }

}
