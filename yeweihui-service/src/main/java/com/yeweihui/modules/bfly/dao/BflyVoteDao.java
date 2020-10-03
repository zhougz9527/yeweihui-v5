package com.yeweihui.modules.bfly.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.bfly.entity.BflyVote;
import com.yeweihui.modules.bfly.entity.BflyVoteVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BflyVoteDao extends BaseMapper<BflyVote> {


    List<Map<String, Object>> selectAllUserVoteResult(HashMap<String, Long> params);

    List<BflyVote> queryBflyVoteList(@Param("zondId") Long zondId,
                                       @Param("title") String title,
                                       @Param("startDate") String startDate,
                                       @Param("endDate") String endDate,
                                       @Param("current") Integer current,
                                       @Param("size") Integer size);

    List<BflyVote> queryCurBflyVoteList(@Param("zondId") Long zondId,
                                     @Param("title") String title,
                                     @Param("startDate") String startDate,
                                     @Param("endDate") String endDate,
                                     @Param("current") Integer current,
                                     @Param("size") Integer size,
                                     @Param("status") Integer status);

    Long queryBflyVoteListCount( @Param("zondId") Long zondId,
                                 @Param("title") String title,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate);

    Long queryCurBflyVoteListCount( @Param("zondId") Long zondId,
                                 @Param("title") String title,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate,
                                 @Param("status") Integer status);

    int updateBflyVoteOfflineVoteResultUrl(@Param("id") Long id,
                                           @Param("offlineVoteResultUrl") String offlineVoteResultUrl);
}
