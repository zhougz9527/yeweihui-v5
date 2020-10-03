package com.yeweihui.modules.vo.bfly.form.vote;

import com.yeweihui.modules.bfly.entity.BflyUserSubVote;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.List;

@Data
@ApiOperation(value = "表决表单")
public class VoteForm {

    @ApiModelProperty(value = "用户表决id")
    private Long userVoteId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "表决id")
    private Long bflyVoteId;

    @ApiModelProperty(value = "参会签名url")
    private String meetingSignatureUrl;

    @ApiModelProperty(value = "参会签名头像url")
    private String meetingSignatureHeaderUrl;

    @ApiModelProperty(value = "表决投票签名url")
    private String voteSignatureUrl;

    @ApiModelProperty(value = "表决投票头像签名url")
    private String voteSignatureHeaderUrl;

    @ApiModelProperty(value = "用户子表决")
    private List<BflyUserSubVote> userSubVotes;


}
