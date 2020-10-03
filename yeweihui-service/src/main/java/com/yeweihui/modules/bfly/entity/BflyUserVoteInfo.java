package com.yeweihui.modules.bfly.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BflyUserVoteInfo extends BaseEntity {

    private Long id;

    private Long bflyUserId;

    private Long bflyVoteId;

    // 参会签名图片url
    private String meetingSignatureUrl;

    // 投票签名图片url
    private String voteSignatureUrl;

    // 提交参会时间
    private Date meetingSubmitTime;

    // 提交投票时间
    private Date voteSubmitTime;

    // 用户参与的状态
    private Integer status;

    // 微信昵称
    private String nickname;

    // 小区id
    private Long zoneId;

    // 小区名
    private String name;

    // 苑
    private String court;

    // 幢
    private String building;

    // 楼号
    private String floorName;

    // 单元号
    private String unitName;

    // 建筑面积
    private BigDecimal housingArea;

    // 表决名称
    private String title;

    //业主姓名
    private String userName;

}
