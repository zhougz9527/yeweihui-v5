package com.yeweihui.modules.bfly.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.xml.soap.Text;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_sub_vote")
public class BflySubVote extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("bfly_vote_id")
    private Long bflyVoteId;

    private String title;

    // 0:单选表决 1:多选一表决
    @TableField("vote_type")
    private Integer voteType;

    // 说明
    private String description;

    // 选项集合
    private String options;

    // 投票结果选项
    @TableField("result_option")
    private String resultOption;

    // 0: 支持大多数意见 1:弃权
    @TableField("default_option")
    private Integer defaultOption;

    // 子表决事项
    @TableField(exist = false)
    private List<BflyVoteItem> bflyVoteItems;

    // 用户子表决投票信息
    @TableField(exist = false)
    private BflyUserSubVote bflyUserSubVote;

    // 子表决的统计信息
    @TableField(exist = false)
    private BflyStatVote bflyStatVote;

//    public String getOptions() {
//        return options;
//    }

//    public void setOptions(String options) {
//        this.options = options;
//    }

//    public void setOptions(JSONArray options) {
//        if (null != options) {
//            this.options = options.toJSONString();
//        } else {
//            this.options = new JSONArray().toJSONString();
//        }
//    }
}
