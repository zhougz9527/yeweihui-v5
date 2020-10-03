package com.yeweihui.modules.user.dao;

import com.yeweihui.modules.user.entity.UserEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.UserQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业委会用户
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
public interface UserDao extends BaseMapper<UserEntity> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户ID获取用户表信息
     */
    UserEntity findUserByUserId(Long userId);

    /**
     * 用户列表
     * @param userQueryParam
     * @return
     */
    List<UserEntity> simpleList(UserQueryParam userQueryParam);

    /**
     * 获取主任角色，获取当前小区主任用户
     * @param zoneId
     * @return
     */
    UserEntity getDirectorByZoneId(@Param("zoneId") Long zoneId);

    /**
     * 获取当前小区管理员用户
     * @param zoneId
     * @return
     */
    UserEntity getManageByZoneId(@Param("zoneId")Long id);

    /**
     * 根据openId获取用户
     * @param openId
     * @return
     */
    UserEntity getByOpenId(@Param("openId") String openId);

    /**
     * 获取用户的组id
     * @param userId
     * @return
     */
    Long findGroupIdByUserId(Long userId);
}
