package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.PaperMemberEntity;
import com.yeweihui.modules.vo.query.PaperMemberQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 函件签收表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface PaperMemberService extends IService<PaperMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PaperMemberEntity getByPidUid(Long pid, Long uid);

    int getCount(PaperMemberQueryParam paperMemberQueryParam);

    List<PaperMemberEntity> getListByPid(Long id);
}

