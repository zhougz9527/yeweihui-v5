package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_user_vote")
public class BflyUserVote extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("bfly_user_id")
    private Long bflyUserId;

    @TableField("bfly_vote_id")
    private Long bflyVoteId;

    // 参会签名图片url
    @TableField("meeting_signature_url")
    private String meetingSignatureUrl;

    // 参会头像图片url
    @TableField("meeting_signature_header_url")
    private String meetingSignatureHeaderUrl;

    // 投票签名图片url
    @TableField("vote_signature_url")
    private String voteSignatureUrl;

    // 投票头像图片url
    @TableField("vote_signature_header_url")
    private String voteSignatureHeaderUrl;

    // 提交参会时间
    @TableField("meeting_submit_time")
    private Date meetingSubmitTime;

    // 提交投票时间
    @TableField("vote_submit_time")
    private Date voteSubmitTime;

    // 用户参与的状态
    @TableField("status")
    private Integer status;

    //参与投票的用户信息
    @TableField(exist = false)
    private BflyUser bflyUser;

}
