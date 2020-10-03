package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_user_sub_vote")
public class BflyUserSubVote extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("bfly_user_id")
    private Long bflyUserId;

    @TableField("bfly_sub_vote_id")
    private Long bflySubVoteId;

    // 投票结果
    @TableField("result_option")
    private String resultOption;

//    @TableField(exist = false)
//    private List<BflyVoteItem> voteItems;



}
