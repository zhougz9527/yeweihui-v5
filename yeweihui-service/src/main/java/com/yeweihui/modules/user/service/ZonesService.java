package com.yeweihui.modules.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.vo.query.ZonesQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 小区
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-03 19:51:04
 */
public interface ZonesService extends IService<ZonesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(ZonesEntity zones);

    void update(ZonesEntity zones);

    ZonesEntity info(Long id);

//    List<ZonesEntity> query();

    ZonesEntity getByInviteCode(String inviteCode);

    List<ZonesEntity> simpleList(ZonesQueryParam zonesQueryParam);

    /**
     * 根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的refund_enable_time，生成倒计时时间
     * @param userEntity
     * @return
     */
    long getRefundEnableLeftDays(UserEntity userEntity);

    List<ZonesEntity> simpleList2(Map<String, Object> params);

    /**
     * 根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的 enableUseTime ，生成使用的倒计时时间
     * @param userEntity
     * @return
     */
    long getEnableUseLeftDays(UserEntity userEntity);

    /**
     * 返回行业主管所掌管的小区List
     */
    List<ZonesEntity> getManagerZoneLists(UserEntity userEntity);

    ZonesEntity getById(Long id);

    List<ZonesEntity> queryByName(String zoneName);
}

