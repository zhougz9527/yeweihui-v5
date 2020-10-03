package com.yeweihui.modules.bfly.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.bfly.dao.BflyRoomDao;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.service.BflyRoomService;
import com.yeweihui.modules.bfly.service.BflyUserCertRecordService;
import com.yeweihui.modules.bfly.service.BflyUserService;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.ZonesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.yeweihui.modules.enums.bfly.MagicEnum.审核中;
import static com.yeweihui.modules.enums.bfly.MagicEnum.审核通过;

@Service("bflyRoomService")
public class BflyRoomServiceImpl extends ServiceImpl<BflyRoomDao, BflyRoom> implements BflyRoomService {

    @Resource
    BflyUserService bflyUserService;
    @Resource
    ZonesService zonesService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page page = new Query<BflyRoom>(params).getPage();
        List<BflyRoom> users = this.baseMapper.selectPage(page, new EntityWrapper<>());
        page.setRecords(users);
        return new PageUtils(page);
    }

    @Override
    public BflyRoom updateRoom(BflyRoom bflyRoom) {
        BflyRoom oldRoom = this.baseMapper.selectById(bflyRoom.getId());
        oldRoom.setZoneId(bflyRoom.getZoneId());
        oldRoom.setCourt(bflyRoom.getCourt());
        oldRoom.setBuilding(bflyRoom.getBuilding());
        oldRoom.setUnitName(bflyRoom.getUnitName());
        oldRoom.setFloorName(bflyRoom.getFloorName());
        oldRoom.setHousingArea(bflyRoom.getHousingArea());
        oldRoom.setUserName(bflyRoom.getUserName());
        oldRoom.setPhoneNum(bflyRoom.getPhoneNum());
        oldRoom.setIdCard(bflyRoom.getIdCard());
        this.baseMapper.updateById(oldRoom);
        return oldRoom;
    }

    //查询房屋结构
    @Override
    public Set<Long> queryRoomStructure(List<Long> zoneIds) {
        List<BflyRoom> bflyRooms = this.baseMapper.selectList(new EntityWrapper<BflyRoom>().in("zone_id", zoneIds).ne("court", ""));
        Set set = new HashSet<>();
        bflyRooms.forEach(bflyRoom -> set.add(bflyRoom.getZoneId()));
        return set;
    }

    // 获取苑
    @Override
    public List<Map<String, String>> queryZoneCourt(Long id) {
        return this.baseMapper.queryZoneCourt(id);
    }

    // 获取幢
    @Override
    public List<Map<String, String>> queryZoneBuilding(Long id, String court) {
        HashMap params = new HashMap(){{put("id", id); put("court", court);}};
        return this.baseMapper.queryZoneBuilding(params);
    }

    // 获取单元 房号
    @Override
    public List<BflyRoom> queryZoneFloorName(Long id, String court, String building) {
        List<Map<String, Object>> mapList = this.baseMapper.queryFloorNameByCourtAndBuilding(id, court, building);
        List<BflyRoom> result = new ArrayList<>();
        mapList.forEach(map -> {
            String floorName = (String) map.get("floor_name");
            String unitName = (String) map.get("unit_name");
            List<BflyRoom> bflyRooms = this.baseMapper.selectList(new EntityWrapper<BflyRoom>()
                    .eq("zone_id", id).eq("court", court).eq("building", building)
                    .eq("floor_name", floorName).eq("unit_name", unitName).orderBy(true, "id").last("limit 1"));
            if (null != bflyRooms && bflyRooms.size() > 0) {
                result.add(bflyRooms.get(0));
            }
        });
        return result;
    }

    @Override
    public List<Map<String, Long>> stateRoomNumByZoneId(Map<String, Object> params) {
        return this.baseMapper.stateRoomNumByZoneId(params);
    }

    @Override
    public List<Map<String, Integer>> snapshotRoomNumByVoteId(Long voteId) {
        return this.baseMapper.snapshotRoomNumByVoteId(voteId);
    }

    @Override
    public List<Map<String, Object>> stateRoomAreaByZoneId(Map<String, Object> params) {
        return this.baseMapper.stateRoomAreaByZoneIdNew(params);
    }

    @Override
    public List<Map<String, Object>> snapshotRoomAreaByVoteId(Long voteId) {
        return this.baseMapper.snapshotRoomAreaByVoteId(voteId);
    }

    @Override
    public PageUtils queryRoom(String userName, String phoneNum, Long zoneId, Integer userStatus, String page, String size) {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("phoneNum", phoneNum);
        params.put("zoneId", zoneId);
        params.put("userStatus", userStatus);
        params.put("page", page);
        params.put("limit", size);
        EntityWrapper<BflyRoom> wrapper = new EntityWrapper<BflyRoom>();
        if (null != zoneId && zoneId > 0) {
            wrapper.eq("zone_id", zoneId);
        }
        if (StringUtils.isNotEmpty(userName)) {
            wrapper.like("user_name", userName);
        }
        if (StringUtils.isNotEmpty(phoneNum)) {
            wrapper.like("phone_num", phoneNum);
        }
        Page roomPage = new Query<BflyUser>(params).getPage();
        List<BflyRoom> rooms = this.baseMapper.selectPageBflyRoom(roomPage, params);
        roomPage.setRecords(rooms);
        return new PageUtils(roomPage);
    }

    @Override
    public BflyRoom info(Long id) {
        BflyRoom bflyRoom = this.baseMapper.selectById(id);
        if (null != bflyRoom) {
            ZonesEntity zonesEntity = zonesService.selectById(bflyRoom.getZoneId());
            bflyRoom.setZoneName(zonesEntity.getName());
        }
        return bflyRoom;
    }

    /**
     * 删除房屋
     * @param id
     */
    @Override
    public void delete(Long id) {
        List<BflyUser> bflyUsers = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("bfly_room_id", id)
                .in("status", Arrays.asList(new Integer[] {(Integer) 审核通过.getCode(), (Integer) 审核中.getCode()})));
        if (null != bflyUsers && bflyUsers.size() > 0) {
            throw new RRException("该房屋已处于审核的流程中");
        }
        this.baseMapper.deleteById(id);
    }

    @Override
    public BigDecimal queryAllArea(Long zoneId) {
        HashMap<String, Object> param = new HashMap<>();
        if (null != zoneId) {
            param.put("zoneId", zoneId);
        }
        List<Map<String, Object>> allAreaList = this.stateRoomAreaByZoneId(param);
        // 总面积
        BigDecimal allArea = new BigDecimal(0);
        for (int i = 0; i < allAreaList.size(); i++) {
            allArea = allArea.add((BigDecimal) allAreaList.get(i).get("total"));
        }
        allArea = allArea.setScale(2,BigDecimal.ROUND_HALF_UP);
        return allArea;
    }

}
