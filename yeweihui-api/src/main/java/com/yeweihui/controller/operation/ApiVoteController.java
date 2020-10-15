package com.yeweihui.controller.operation;

import com.yeweihui.annotation.GetUser;
import com.yeweihui.annotation.Login;
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.bfly.entity.BflyVote;
import com.yeweihui.modules.bfly.service.BflyVoteService;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.jmkj.service.impl.JmkjServiceImpl;
import com.yeweihui.modules.operation.entity.VoteEntity;
import com.yeweihui.modules.operation.service.HisViewLogService;
import com.yeweihui.modules.operation.service.VoteService;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.VoteForm;
import com.yeweihui.modules.vo.api.form.vote.VoteCancelForm;
import com.yeweihui.modules.vo.query.VoteQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 事务表决表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
@RestController
@RequestMapping("/api/operation/vote")
@Api(tags="事务表决")
public class ApiVoteController {

    @Autowired
    JmkjServiceImpl jmkjServiceImpl;

    @Autowired
    private VoteService voteService;
    @Autowired
    private HisViewLogService hisViewLogService;
    @Autowired
    BflyVoteService bflyVoteService;

    @Login
    @PostMapping("/list")
    @ApiOperation("分页列表")
    @RequiresPermissions("operation:vote:list")
    public R list(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody VoteQueryParam voteQueryParam){
        voteQueryParam.setViewUid(userEntity.getId());
        PageUtils page = voteService.queryPage(BeanUtil.bean2map(voteQueryParam));

        return R.ok().put("page", page);
    }

    @PostMapping({"/info/{id}", "/info/{type}/{id}"})
    @ApiOperation("信息")
    public R info(@ApiIgnore @GetUser UserEntity userEntity,
                  @PathVariable("id") Long id,
                  @PathVariable(name = "type", required = false) String type){
        VoteEntity vote = voteService.info(id, userEntity);

        vote.setTimeQuitNum(jmkjServiceImpl.TimeVote(vote.getId()));
        vote.setNoTimeQuitNum(jmkjServiceImpl.NoTimeVote(vote.getId()));

        if (userEntity != null && type != null && "history".equals(type)) {
            hisViewLogService.save(BizTypeEnum.VOTE, userEntity, id);
        }

        return R.ok().put("vote", vote);
    }

    @Login
    @PostMapping("/save")
    @ApiOperation("保存")
    @RequiresPermissions("operation:vote:save")
    public R save(@ApiIgnore @LoginUser UserEntity userEntity,
                  @RequestBody VoteEntity vote) {
        vote.setUid(userEntity.getId());
        voteService.save(vote);

        return R.ok();
    }

    @Login
    @PostMapping("/update")
    @ApiOperation("修改")
    @RequiresPermissions("operation:vote:update")
    public R update(@RequestBody VoteEntity vote){
        ValidatorUtils.validateEntity(vote);
        voteService.update(vote);

        return R.ok();
    }

    @Login
    @PostMapping("/delete")
    @ApiOperation("删除")
    @RequiresPermissions("operation:vote:delete")
    public R delete(@RequestBody Long[] ids){
        List<VoteEntity> voteEntities = new ArrayList<>();
        for (Long id: ids) {
            VoteEntity voteEntity = new VoteEntity();
            voteEntity.setId(id);
            voteEntity.setRecordStatus(0);
            voteEntities.add(voteEntity);
        }
        voteService.insertOrUpdateBatch(voteEntities);

        return R.ok();
    }

    /****************************************************************
     *                            具体操作
     ****************************************************************/

    @Login
    @PostMapping("/vote")
    @ApiOperation("用户表决")
    public R vote(@ApiIgnore @LoginUser UserEntity user,
                  @RequestBody VoteForm voteForm){
        voteForm.setUid(user.getId());
        voteService.vote(voteForm);

        return R.ok();
    }

    @Login
    @PostMapping("/voteCancel")
    @ApiOperation("用户撤销投票")
    public R voteCancel(@ApiIgnore @LoginUser UserEntity user,
                  @RequestBody VoteCancelForm voteCancelForm){
        voteCancelForm.setUid(user.getId());
        voteService.voteCancel(voteCancelForm);

        return R.ok();
    }

    @PostMapping("/result/{id}")
    @ApiOperation("表决统计结果")
    public R ownersMeeting(@PathVariable("id") Long id) {
        BflyVote bflyVote = bflyVoteService.selectStateByBflyVoteId(id);
        return R.ok().put("result", bflyVote);
    }

    @PostMapping("/historyVote/{id}")
    @ApiOperation("历史投票")
    public R historyVote(@PathVariable("id") Long id) {
        List<BflyVote> list = bflyVoteService.historyVote(id);
        return R.ok().put("result", list);
    }

}
