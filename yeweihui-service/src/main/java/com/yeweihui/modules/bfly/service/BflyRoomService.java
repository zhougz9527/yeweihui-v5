package com.yeweihui.modules.bfly.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BflyRoomService extends IService<BflyRoom> {

    PageUtils queryPage(Map<String, Object> params);

    BflyRoom updateRoom(BflyRoom bflyRoom);

    //查询房屋结构
    Set<Long> queryRoomStructure(List<Long> zoneIds);

    //查询苑
    List<Map<String, String>> queryZoneCourt(Long id);

    //查询幢
    List<Map<String, String>> queryZoneBuilding(Long id, String court);

    //查询单元 房号
    List<BflyRoom> queryZoneFloorName(Long id, String court, String building);

    // 统计小区户数
    List<Map<String, Long>> stateRoomNumByZoneId(Map<String, Object> params);

    List<Map<String, Integer>> snapshotRoomNumByVoteId(Long voteId);

    //统计小区面积
    List<Map<String, Object>> stateRoomAreaByZoneId(Map<String, Object> params);

    public List<Map<String, Object>> snapshotRoomAreaByVoteId(Long voteId);

    PageUtils queryRoom(String userName, String phoneNum, Long zoneId, Integer roomStatus, String page, String size);

    BflyRoom info(Long id);

    void delete(Long id);

    /**
     * 查询某小区已导入的房屋总面积
     * @param zoneId
     * @return allArea
     */
    BigDecimal queryAllArea(Long zoneId);

}
