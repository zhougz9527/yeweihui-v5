package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.VoteEntity;
import com.yeweihui.modules.oss.entity.SysOssEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.VoteForm;
import com.yeweihui.modules.vo.api.form.vote.VoteCancelForm;
import com.yeweihui.modules.vo.query.VoteQueryParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 事务表决表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface VoteService extends IService<VoteEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(VoteEntity vote);

    VoteEntity info(Long id, UserEntity userEntity);

    void update(VoteEntity vote);

    int getCount(VoteQueryParam voteQueryParam);

    void vote(VoteForm voteForm);

    /**
     * 获取出投票未完成的，超过投票截止时间的，默认审核弃权；并更新表决状态
     */
    public void expireVote();

    List<VoteEntity> simpleList(VoteQueryParam voteQueryParam);

    /**
     * 用户撤销投票
     * @param voteCancelForm
     */
    void voteCancel(VoteCancelForm voteCancelForm);

    void pushMessage();
}

