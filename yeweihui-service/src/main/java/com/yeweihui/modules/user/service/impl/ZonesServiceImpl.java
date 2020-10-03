package com.yeweihui.modules.user.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.service.BflyRoomService;
import com.yeweihui.modules.division.entity.DivisionManagerEntity;
import com.yeweihui.modules.division.service.DivisionManagerService;
import com.yeweihui.modules.sys.dao.SysRoleDao;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.query.ZonesQueryParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.user.dao.ZonesDao;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;

import javax.annotation.PostConstruct;

@EnableScheduling
@Service("zonesService")
public class ZonesServiceImpl extends ServiceImpl<ZonesDao, ZonesEntity> implements ZonesService {

    private static final Logger logger = LoggerFactory.getLogger(ZonesServiceImpl.class);

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    ZonesService zonesService;
    @Autowired
    DivisionManagerService divisionManagerService;
    @Autowired
    BflyRoomService bflyRoomService;

    private Map<Long, ZonesEntity> zonesEntityMap;

    @PostConstruct
    private void init() {
        initZonesMap();
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    private void initZonesMap() {
        List<ZonesEntity> list = selectList(new EntityWrapper<>());
        Map<Long, ZonesEntity> tmp = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (ZonesEntity item : list) {
                tmp.put(item.getId(), item);
            }
        }
        zonesEntityMap = tmp;
        logger.info("reload zones map success");
    }


    @Override
    @ZoneFilter
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Query(params).getPage();
        List<ZonesEntity> list = this.baseMapper.selectPageEn(page, params);
        page.setRecords(list);

        return new PageUtils(page);
    }


    @Override
    public void save(ZonesEntity zones) {
        String inviteCode = this.getInviteCode();
        zones.setInviteCode(inviteCode);
        zones.setRegisterTime(new Date());
        this.insert(zones);
    }

    @Override
    public void update(ZonesEntity zones) {
        if (StringUtils.isBlank(zones.getInviteCode())){
            String inviteCode = this.getInviteCode();
            zones.setInviteCode(inviteCode);
        }
        this.updateAllColumnById(zones);//全部更新
    }

    @Override
    public ZonesEntity info(Long id) {
        ZonesEntity zones = this.selectById(id);
        if (zones != null && StringUtils.isBlank(zones.getInviteCode())){
            String inviteCode = this.getInviteCode();
            zones.setInviteCode(inviteCode);
            this.updateById(zones);
        }
        return zones;
    }

    private String getInviteCode(){
        int randomInt = RandomUtil.randomInt(1, 999999);
        String inviteCode = String.format("%06d", randomInt);
        ZonesEntity zonesEntity = this.baseMapper.getByInviteCode(inviteCode);
        while(null != zonesEntity){
            randomInt = RandomUtil.randomInt(1, 999999);
            inviteCode = String.format("%06d", randomInt);
            zonesEntity = this.baseMapper.getByInviteCode(inviteCode);
        }
        return inviteCode;
    }

    @Override
    public ZonesEntity getById(Long id) {
        return zonesEntityMap.get(id);
    }


    /**
     * 根据名字查询小区
     * @param zoneName
     * @return
     */
    @Override
    public List<ZonesEntity> queryByName(String zoneName) {
        HashMap params = new HashMap(){{put("zoneName", zoneName);}};
        List<ZonesEntity> zonesEntities = this.baseMapper.queryZoneByName(params);
        List<Long> zoneIds = new ArrayList<>();
        zonesEntities.forEach(zonesEntity -> zoneIds.add(zonesEntity.getId()));
        Set<Long> zoneIdSets = bflyRoomService.queryRoomStructure(zoneIds);// 有苑结构的小区Id
        zonesEntities.forEach(zonesEntity -> {
            if (zoneIdSets.contains(zonesEntity.getId())) {
                zonesEntity.setCourtStructure(true);
            }
        });
        return zonesEntities;
    }

    @Override
    public ZonesEntity getByInviteCode(String inviteCode){
        return this.baseMapper.getByInviteCode(inviteCode);
    }

    @Override
    public List<ZonesEntity> simpleList(ZonesQueryParam zonesQueryParam) {
        return this.baseMapper.simpleList(zonesQueryParam);
    }

    /**
     * 根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的refund_enable_time，生成倒计时时间
     * @param userEntity
     * @return
     */
    @Override
    public long getRefundEnableLeftDays(UserEntity userEntity) {
        List<Long> sysUserRoleList = sysUserRoleService.queryRoleIdList(userEntity.getId());
        if (!sysUserRoleList.contains(1L)){
            //如果角色中没有小区管理员的角色
            throw new RRException("当前用户非小区管理员，不能查看");
        }
        Long zoneId = userEntity.getZoneId();
        ZonesEntity zonesEntity = zonesService.selectById(zoneId);
        Date refundEnableTime = zonesEntity.getRefundEnableTime();
        long days = DateUtil.between(new Date(), refundEnableTime, DateUnit.DAY);
        if (days < 0){
            return 0L;
        }
        return days;
    }

    @Override
    @ZoneFilter
    public List<ZonesEntity> simpleList2(Map<String, Object> params) {
        Boolean operateManager = (Boolean) params.get(Constant.OPERATE_MANAGER);
        if (BooleanUtils.isTrue(operateManager)) {
            return this.baseMapper.simpleList2(null);
        } else {
            return this.baseMapper.simpleList2(params);
        }
    }

    /**
     * 根据用户token，判断是不是管理员，是的话根据用户中的zoneId获取zone中的 enableUseTime ，生成使用的倒计时时间
     * @param userEntity
     * @return
     */
    @Override
    public long getEnableUseLeftDays(UserEntity userEntity) {
        List<Long> sysUserRoleList = sysUserRoleService.queryRoleIdList(userEntity.getId());
        if (!sysUserRoleList.contains(1L)){
            //如果角色中没有小区管理员的角色
            throw new RRException("当前用户非小区管理员，不能查看");
        }
        Long zoneId = userEntity.getZoneId();
        ZonesEntity zonesEntity = zonesService.selectById(zoneId);
        Date enableUseTime = zonesEntity.getEnableUseTime();
        long days = DateUtil.between(new Date(), enableUseTime, DateUnit.DAY);
        if (days < 0){
            return 0L;
        }
        return days;
    }

    @Override
    public List<ZonesEntity> getManagerZoneLists(UserEntity userEntity) {
        String code = userEntity.getRoleCode();
        if (!"2000".equals(code)) {
            throw new RRException("只有行业主管用户有权访问");
        }
        DivisionManagerEntity divisionManagerEntity = divisionManagerService.selectOne(
                new EntityWrapper<DivisionManagerEntity>().eq("user_id", userEntity.getId())
        );
        List<ZonesEntity> zonesEntities = new ArrayList<>();
        String level = divisionManagerEntity.getLevel();
        Long divisionId = divisionManagerEntity.getDivisionId();
        if ("province".equals(level)) {
            zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("province_id", divisionId)));
        } else if ("city".equals(level)) {
            zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("city_id", divisionId)));
        } else if ("district".equals(level)) {
            zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("district_id", divisionId)));
        } else if ("subdistrict".equals(level)) {
            zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("subdistrict_id", divisionId)));
        } else if ("community".equals(level)) {
            zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("community_id", divisionId)));
        }
        return zonesEntities;

    }

}
