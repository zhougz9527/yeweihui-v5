package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yeweihui.modules.bfly.entity.BflyRoom;
import com.yeweihui.modules.bfly.entity.BflyUser;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyUserDao extends BaseMapper<BflyUser> {

    List<BflyUser> selectPageVO(Page page, Map<String, Object> params);

    /**
     * @Description: 查询业主列表(已认证和认证撤销的)
     * @Param:
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/13
     */
    List<HashMap<String,Object>> queryOwnerInfo(
                                 @Param("ownerName") String ownerName,
                                 @Param("phone") String phone,
                                 @Param("zoneId") Long zoneId,
                                 @Param("statusArray") Integer[] statusArray,
                                 @Param("current") Integer current,
                                 @Param("size") Integer size);

//    List<Map<String, Object>> queryRoomUser(@Param("username") String username, @Param("phoneNum") String phoneNum, @Param("zoneId") Long zoneId, @Param("current") Integer current, @Param("size") Integer size);

    /**
     * @Description: 条件查询符合条件的业主数量
     * @Param:
     * @return:
     * @Author: tenaciousVine
     * @Date: 2020/3/15
     */
    Long queryOwnerInfoCount(@Param("ownerName") String ownerName,
                             @Param("phone") String phone,
                             @Param("zoneId") Long zoneId,
                             @Param("statusArray") Integer[] statusArray);

    Map<String,Object> searchCertAreaAndCount(@Param("zoneId") Long zoneId);

    Map<String, Object> sumMeetingArea(@Param("voteId") Long voteId);

    List<BflyUser> findBflyUserByVoteIdAndUserVoteStatus(Long voteId, Integer userVoteStatus);
}
