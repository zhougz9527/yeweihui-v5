package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_vote")
public class BflyVote extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("zone_id")
    private Long zoneId;

    // 表决名称
    private String title;

    // 投票发起时小区总面积
    @TableField("total_area_snapshot")
    private BigDecimal totalAreaSnapshot;

    // 发起投票时小区总户数
    @TableField("room_num_snapshot")
    private Integer roomNumSnapshot;

    // 开始参会时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("meeting_start_time")
    private Date meetingStartTime;

    // 结束参会时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("meeting_end_time")
    private Date meetingEndTime;

    // 开始投票时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("vote_start_time")
    private Date voteStartTime;

    // 结束投票时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("vote_end_time")
    private Date voteEndTime;

    // 图片
    @TableField("offline_vote_result_url")
    private String offlineVoteResultUrl;

    // 1: 未开始 2:参会中 3:参会结束 4:投票中 5:投票结束
    private Integer status;

    // 0:隐藏 1:显示
    @TableField("is_show")
    private Integer isShow;

    // 子表决列表
    @TableField(exist = false)
    private List<BflySubVote> bflySubVotes;

    // 用户投票信息
    @TableField(exist = false)
    private BflyUserVote bflyUserVote;

    // 表决的结果
    @TableField(exist = false)
    private List<BflyStatVote> bflyStatVoteList;
    // 小区名称

    @TableField(exist = false)
    private String zoneName;

}
