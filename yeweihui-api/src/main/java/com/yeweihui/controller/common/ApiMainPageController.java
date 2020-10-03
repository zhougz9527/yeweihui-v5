package com.yeweihui.controller.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.utils.R;
import com.yeweihui.modules.common.service.MainPageService;
import com.yeweihui.modules.operation.entity.MenuMapEntity;
import com.yeweihui.modules.sys.entity.SysMenuEntity;
import com.yeweihui.modules.sys.service.SysMenuService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZoneGroupEntity;
import com.yeweihui.modules.user.entity.ZoneMenuMapEntity;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZoneGroupService;
import com.yeweihui.modules.user.service.ZoneMenuMapService;
import com.yeweihui.modules.vo.api.vo.MainPage1VO;
import com.yeweihui.modules.vo.api.vo.ModuleVO;
import com.yeweihui.modules.vo.query.UserIdQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/mainPage")
@Api(tags="首页")
public class ApiMainPageController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MainPageService mainPageService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private UserService userService;

    @Autowired
    private ZoneMenuMapService zoneMenuMapService;

    @Autowired
    private ZoneGroupService zoneGroupService;

//    private static Map<String, List<String>> historyMenuMap = new HashMap<String, List<String>>() {{
//        put("list", new ArrayList<String>(){{ add("stamp"); add("vote"); add("meeting");
//        add("mypaper");add("mybill"); add("mynotice"); }});
//    }};

    @PostMapping("/mainPage1")
    @ApiOperation("首页头部信息1（小区信息，待我用章审批数量，待我事项表决数量，网上发函数量，费用报销数量）")
    public R mainPage1(@RequestBody UserIdQueryParam userIdQueryParam){
        MainPage1VO mainPage1VO = mainPageService.mainPage1(userIdQueryParam);
        return R.ok().put("mainPage1VO", mainPage1VO);
    }

    @PostMapping("/modules")
    @ApiOperation("获取菜单")
    public R modules(@RequestBody UserIdQueryParam userIdQueryParam){
        Long userId = userIdQueryParam.getUid();
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(userId);
        UserEntity userEntity = userService.info(userId);
        String group = userService.getGroupByRoleName(userEntity.getRoleName());

        Map<String, List<MenuMapEntity>> menuListMap = sysMenuService.findMenuMapByUserId(userId);
        List<MenuMapEntity> menuMapEntityList = sysMenuService.getMenuMapEntityListBySysMenuList(menuList, group, menuListMap);

        Map<String, ModuleVO> tmp = new HashMap<>();
        menuMapEntityList.forEach(m -> {
            String pageName = m.getMaMenuPage();
            if (tmp.containsKey(pageName)) {
                putToModuleMap(tmp.get(pageName).getModuleMap(), m);
            } else {
                ModuleVO moduleVO = new ModuleVO(pageName);
                putToModuleMap(moduleVO.getModuleMap(), m);
                tmp.put(pageName, moduleVO);
            }
        });

        Map<String, Map<String, List<String>>> ret = new HashMap<>();
        for (Map.Entry<String, ModuleVO> item : tmp.entrySet()) {
            ret.put(item.getKey(), item.getValue().getModuleMap());
        }

        if (!ret.isEmpty()) {
            // 所有人都可以看历史
            ret.get("index").get("grid").add("history");
            ret.get("my").get("list").add("org");
        }
//         蝴蝶投票小程序添加的逻辑
        ZoneGroupEntity zoneGroupEntity = zoneGroupService.selectOne(new EntityWrapper<ZoneGroupEntity>().eq("group_name", group));
        List<ZoneMenuMapEntity> zoneMenuMapEntities = zoneMenuMapService.selectListByZoneIdAndGroupId(userEntity.getZoneId(), zoneGroupEntity.getId());
        if (null != zoneMenuMapEntities) {
            String menuMapIds = zoneMenuMapEntities.get(0).getMenuMapIds();
            List<String> ids = Arrays.asList(menuMapIds.split(","));
            if (ids.contains("44")) {
                ret.get("history").get("list").add("ownmeeting");// 业主大会
            }
        }
        return R.ok().put("menuMap", ret);
    }


    private void putToModuleMap(Map<String, List<String>> moduleMap, MenuMapEntity menuMapEntity) {
        String maMenuType = menuMapEntity.getMaType();
        if (moduleMap.containsKey(maMenuType)) {
            moduleMap.get(maMenuType).add(menuMapEntity.getMaMenuName());
        } else {
            List<String> l = new ArrayList<>();
            l.add(menuMapEntity.getMaMenuName());
            moduleMap.put(maMenuType, l);
        }
    }



}
