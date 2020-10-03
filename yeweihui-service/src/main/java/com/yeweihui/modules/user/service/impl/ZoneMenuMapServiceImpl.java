package com.yeweihui.modules.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.modules.user.dao.ZoneGroupDao;
import com.yeweihui.modules.user.dao.ZoneMenuMapDao;
import com.yeweihui.modules.user.entity.ZoneGroupEntity;
import com.yeweihui.modules.user.entity.ZoneMenuMapEntity;
import com.yeweihui.modules.user.service.ZoneMenuMapService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("zoneMenuMapService")
public class ZoneMenuMapServiceImpl extends ServiceImpl<ZoneMenuMapDao, ZoneMenuMapEntity> implements ZoneMenuMapService {

    @Resource
    ZoneGroupDao zoneGroupDao;

    @Override
    public List<ZoneMenuMapEntity> selectListByZoneIdAndGroupId(Long zoneId, Long groupId) {
        return this.baseMapper.selectListByZoneIdAndGroupId(zoneId, groupId);
    }

    @Override
    public void insertByZoneId(Long zoneId) {
        Wrapper<ZoneMenuMapEntity> zoneMenuMapEntityWrapper = new EntityWrapper<>();
        zoneMenuMapEntityWrapper.eq("zone_id", zoneId);
        List<ZoneMenuMapEntity> zoneMenuMapEntityList = this.baseMapper.selectList(zoneMenuMapEntityWrapper);
        if (null != zoneMenuMapEntityList && zoneMenuMapEntityList.size() > 0) {// 已经有该小区的配置
            return ;
        }
        Wrapper<ZoneGroupEntity> wrapper = new EntityWrapper<>();
        List<ZoneGroupEntity> zoneGroupEntityList = zoneGroupDao.selectList(wrapper);
        // 小区业主、物业公司
        String ownerMenuMapIds = "2,14,20,8,42,18,23,24,15,16,17,3,4,5,6,7,9,10,11,12,19,21,22,33,28,29,30,31,32,41,43,35,34,37,36,38,39,40";
        // 业委会
        String committeeMenuMapIds = "2,14,20,8,42,18,23,24,15,16,17,3,4,5,6,7,9,10,11,12,19,21,22,33,28,27,29,30,31,32,41,43,35,34,37,36,38,39,40";
        // 运营管理
        String operationMenuMapIds = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43";
        // 行业主管
        String managerMenuMapIds = "25,27,28,29,30,31,32,33,39,41,43";
        zoneGroupEntityList.forEach(zoneGroupEntity -> {
            Long groupId = zoneGroupEntity.getId();
            ZoneMenuMapEntity zoneMenuMapEntity = new ZoneMenuMapEntity();
            if (1 == groupId) {// 业委会
                zoneMenuMapEntity.setMenuMapIds(committeeMenuMapIds);
            }
            if (2 == groupId || 3 == groupId) {// 小区业主、物业公司
                zoneMenuMapEntity.setMenuMapIds(ownerMenuMapIds);
            }
            if (4 == groupId) {// 运营管理
                zoneMenuMapEntity.setMenuMapIds(operationMenuMapIds);
            }
            if (groupId == 5) {// 行业主管
                zoneMenuMapEntity.setMenuMapIds(managerMenuMapIds);
            }
            zoneMenuMapEntity.setZoneId(zoneId);
            zoneMenuMapEntity.setGroupId(groupId);
            baseMapper.insert(zoneMenuMapEntity);
        });
    }
}
