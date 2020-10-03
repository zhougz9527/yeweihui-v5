package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyUser;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public interface BflyRoomDao extends BaseMapper<BflyRoom> {

    List<Map<String, String>> queryZoneStructure(Map<String, String> params);

    List<Map<String, String>> queryZoneCourt(Long id);

    List<Map<String, String>> queryZoneBuilding(Map<String, Object> params);

    List<Map<String, Object>> queryZoneFloorName(Map<String, Object> params);

    List<Map<String, Long>> stateRoomNumByZoneId(Map<String, Object> params);

    List<Map<String, Integer>> snapshotRoomNumByVoteId(Long voteId);

//    List<Map<String, Object>> stateRoomAreaByZoneId(Map<String, Object> params);

    List<Map<String, Object>> stateRoomAreaByZoneIdNew(Map<String, Object> params);

    List<Map<String, Object>> snapshotRoomAreaByVoteId(Long voteId);

    Map<String,Object> searchTotalAreaAndCount(Long zoneId);

    List<Map<String, Object>> queryFloorNameByCourtAndBuilding(Long zoneId, String court, String building);

    Integer isCertified(Long zoneId, String court, String building, String unitName, String floorName);

    List<BflyRoom> selectPageBflyRoom(Page page, Map<String, Object> params);
}
