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
@TableName("bfly_vote_item")
public class BflyVoteItem extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("bfly_sub_vote_id")
    private Long bflySubVoteId;

    // 表决事项
    private String matter;

    // 主要内容
    private String content;

    // 附件的url集合
    @TableField("attachment_urls")
    private String attachmentUrls;

}
