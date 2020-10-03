package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_token")
public class BflyToken extends BaseEntity {

    // 用户id
    @TableId(type= IdType.INPUT)
    private Long userId;

    // 登录的token
    private String token;

    // 过期时间
    @TableField("expire_time")
    private Timestamp expireTime;

}
