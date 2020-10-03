package com.yeweihui.modules.user.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.user.entity.ZoneMenuMapEntity;

import java.util.List;

/**
 * 小区菜单权限关联表
 */
public interface ZoneMenuMapDao extends BaseMapper<ZoneMenuMapEntity> {

    List<ZoneMenuMapEntity> selectListByZoneIdAndGroupId(Long zoneId, Long groupId);

    /**
     * 查询用户所在小区的所有菜单ID
     */
//    List<Long> queryUserZoneAllMenuId(Long userId);

    /**
     * 行业主管获取菜单权限ID
     */
//    List<Long> queryManagerUserZoneAllMenuId();

    /**
     * 根据用户获取
     */
    ZoneMenuMapEntity findZoneMenuMapByUserId(Long userId, Long groupId);

}
