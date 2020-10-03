package com.yeweihui.modules.operation.dao;

import com.yeweihui.modules.operation.entity.PaperMemberEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.vo.query.PaperMemberQueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 函件签收表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface PaperMemberDao extends BaseMapper<PaperMemberEntity> {

    PaperMemberEntity getByPidUid(@Param("pid") Long pid, @Param("uid") Long uid);

    int getCount(PaperMemberQueryParam paperMemberQueryParam);

    List<PaperMemberEntity> getListByPid(@Param("pid") Long pid);
}
