package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_stat_vote")
public class BflyStatVote extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("bfly_vote_id")
    private Long bflyVoteId;

    @TableField("bfly_sub_vote_id")
    private Long bflySubVoteId;

    // 子表决名称
    @TableField("bfly_sub_vote_title")
    private String bflySubVoteTitle;

    // 总共投票户数
    @TableField("total_vote_quantity")
    private Integer totalVoteQuantity;

    // 总共投票面积
    @TableField("total_vote_area")
    private BigDecimal totalVoteArea;

    // 选项集合
    private String options;

    // 未认证用户统计结果
    @TableField("uncert_result_json")
    private String uncertResultJson;

    // 参会用户统计结果
    @TableField("meeting_result_json")
    private String meetingResultJson;

    // 投票户数统计结果
    @TableField("quantity_result_json")
    private String quantityResultJson;

    // 投票面积统计结果
    @TableField("area_result_json")
    private String areaResultJson;



}
