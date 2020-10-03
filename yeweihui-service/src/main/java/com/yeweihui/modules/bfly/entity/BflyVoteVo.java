package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: yeweihui-server
 * @description: 用户投票表VO类
 * @author: tenaciousVine
 * @create: 2020-03-20 01:24
 **/
@Data
public class BflyVoteVo extends BflyVote {

    // 未认证户数
    private Long unCertUserCount;

    // 小区总户数
    private Long totalUserCount;

    //已参与户数
    private Long involvedCount;

    //未参与户数
    private Long unInvolvedCount;


    // 未认证面积
    private BigDecimal unCertUserArea;

    // 小区总面积
    private BigDecimal totalUserArea;

    //已参与面积
    private BigDecimal involvedArea;

    //未参与面积
    private BigDecimal unInvolvedArea;
}
