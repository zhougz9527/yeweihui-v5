package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_vote_option")
public class BflyVoteOption extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 选项名称
    @TableField("option_name")
    private String optionName;



}
