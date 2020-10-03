/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yeweihui.modules.sys.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.common.utils.MapUtils;
import com.yeweihui.modules.operation.dao.MenuMapDao;
import com.yeweihui.modules.operation.entity.MenuMapEntity;
import com.yeweihui.modules.sys.dao.SysMenuDao;
import com.yeweihui.modules.sys.entity.SysMenuEntity;
import com.yeweihui.modules.sys.service.MenuMapService;
import com.yeweihui.modules.sys.service.SysMenuService;
import com.yeweihui.modules.sys.service.SysRoleMenuService;
import com.yeweihui.modules.sys.service.SysUserService;
import com.yeweihui.modules.user.dao.UserDao;
import com.yeweihui.modules.user.dao.ZoneGroupDao;
import com.yeweihui.modules.user.dao.ZoneMenuMapDao;
import com.yeweihui.modules.user.entity.ZoneGroupEntity;
import com.yeweihui.modules.user.entity.ZoneMenuMapEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@EnableScheduling
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

	private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private MenuMapService menuMapService;
	@Resource
	private MenuMapDao menuMapDao;
	@Resource
	private ZoneMenuMapDao zoneMenuMapDao;
	@Resource
	private ZoneGroupDao zoneGroupDao;
	@Resource
	private UserDao userDao;

	/** key: sysMenuName, value: list of mapping to ma module.*/
	private Map<String, List<MenuMapEntity>> pageMenuMap;
/**  注释时间 2020-02-19 start */
//	@PostConstruct
//	private void init() {
//		initPageMenu();
//	}
//
//	@Scheduled(cron = "0 0/10 * * * ?")
//	public void initPageMenu() {
//		List<MenuMapEntity> list = menuMapService.selectList(new EntityWrapper<>());
//		Map<String, List<MenuMapEntity>> tmp = new HashMap<>();
//		if (CollectionUtils.isNotEmpty(list)) {
//			String sysMenuName;
//			for (MenuMapEntity menuMapEntity : list) {
//				sysMenuName = menuMapEntity.getSysMenuName();// 系统菜单名称
//				if (tmp.containsKey(sysMenuName)) {
//					tmp.get(sysMenuName).add(menuMapEntity);
//				} else {
//					List<MenuMapEntity> l = new ArrayList<>();
//					l.add(menuMapEntity);
//					tmp.put(sysMenuName, l);
//				}
//			}
//		} else {
//			tmp = Collections.emptyMap();
//		}
//		pageMenuMap = tmp;
//		logger.info("reload menu map success.");
//	}
/**  end  */

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<SysMenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<SysMenuEntity> userMenuList = new ArrayList<>();
		for(SysMenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<SysMenuEntity> queryListParentId(Long parentId) {
		return baseMapper.queryListParentId(parentId);
	}

	@Override
	public List<SysMenuEntity> queryNotButtonList() {
		return baseMapper.queryNotButtonList();
	}

	@Override
	public List<SysMenuEntity> getUserMenuList(Long userId) {
		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null);
		}
		
		//用户菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList);
	}

	@Override
	public List<MenuMapEntity> getMenuMapListByUserId(Long userId) {
		Long groupId = userDao.findGroupIdByUserId(userId);
		ZoneMenuMapEntity zoneMenuMapEntity = zoneMenuMapDao.findZoneMenuMapByUserId(userId, groupId);
		if (null != zoneMenuMapEntity) {
			String menuMapIds = zoneMenuMapEntity.getMenuMapIds();
			if (StringUtils.isNotEmpty(menuMapIds)) {
				ZoneGroupEntity zoneGroupEntity = zoneGroupDao.selectZoneGroupById(zoneMenuMapEntity.getGroupId());
				String[] menuMapIdArray = menuMapIds.split(",");
				List<String> menuMapIdList = Arrays.asList(menuMapIdArray);
				List<MenuMapEntity> menuMapEntityList = menuMapDao.selectMenuMapIdIn(menuMapIdList);
				menuMapEntityList.forEach(menuMapEntity ->menuMapEntity.setAllowGroup(zoneGroupEntity.getGroupName()));
				return menuMapEntityList;
			}
		}
		return null;
	}

	@Override
	public Map<String, List<MenuMapEntity>> findMenuMapByUserId(Long userId) {
		List<MenuMapEntity> list = getMenuMapListByUserId(userId);
		Map<String, List<MenuMapEntity>> menuListMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(list)) {
			String sysMenuName;
			for (MenuMapEntity menuMapEntity : list) {
				sysMenuName = menuMapEntity.getSysMenuName();// 系统菜单名称
				if (menuListMap.containsKey(sysMenuName)) {
					menuListMap.get(sysMenuName).add(menuMapEntity);
				} else {
					List<MenuMapEntity> l = new ArrayList<>();
					l.add(menuMapEntity);
					menuListMap.put(sysMenuName, l);
				}
			}
		} else {
			menuListMap = Collections.emptyMap();
		}
		return menuListMap;
	}

	@Override
	public void delete(Long menuId){
		//删除菜单
		this.deleteById(menuId);
		//删除菜单与角色关联
		sysRoleMenuService.deleteByMap(new MapUtils().put("menu_id", menuId));
	}

	@Override
	public List<MenuMapEntity> getMenuMapEntityListBySysMenuList(List<SysMenuEntity> parentMenuList, String group, Map<String, List<MenuMapEntity>> menuMap) {
		if (StringUtils.isBlank(group)) {
			logger.error("没有找到用户的组别，无法确定对应的菜单,默认使用业委会的配置!");
			group = "业主委员会";
		}
		List<MenuMapEntity> ret = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(parentMenuList)) {
			for (SysMenuEntity catalog : parentMenuList) {
				if (catalog.getType() == Constant.MenuType.CATALOG.getValue()) {
					for (Object o : catalog.getList()) {
						SysMenuEntity menu = (SysMenuEntity) o;
						if (menu.getType() == Constant.MenuType.MENU.getValue()) {
							if (menuMap.get(menu.getName()) != null) {
								for (MenuMapEntity item : menuMap.get(menu.getName())) {
									if (item.getAllowGroup().contains(group)) {
										ret.add(item);
									}
								}
							}
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList){
		//查询根菜单列表
//		if (null != menuIdList) {// 写死蝴蝶投票的菜单
//			Long[] newIds = new Long[]{223L,224L,225L,226L,227L,228L,229L,230L,231L};
//			menuIdList.addAll(Arrays.asList(newIds));
//		}
		List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList);
		return menuList;
	}

	/**
	 * 递归
	 */
	private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList){
		List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
		
		for(SysMenuEntity entity : menuList){
			//目录
			if(entity.getType() == Constant.MenuType.CATALOG.getValue()){
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}
}
