package com.yeweihui.modules.user.dao;

import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.ZonesQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 小区
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
public interface ZonesDao extends BaseMapper<ZonesEntity> {

    /**
     * 根据邀请码获取小区信息
     * @param inviteCode
     * @return
     */
    ZonesEntity getByInviteCode(@Param("inviteCode") String inviteCode);

    List<ZonesEntity> selectPageEn(Page page, Map<String, Object> params);

    List<ZonesEntity> simpleList(ZonesQueryParam zonesQueryParam);

    List<ZonesEntity> simpleList2(Map<String, Object> params);

    // 根据小区名字查询小区数据
    List<ZonesEntity> queryZoneByName(Map<String, String> params);
}
