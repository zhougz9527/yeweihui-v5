package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.AnnounceMemberEntity;
import com.yeweihui.modules.vo.query.AnnounceMemberQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 函件签收表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface AnnounceMemberService extends IService<AnnounceMemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AnnounceMemberEntity> simpleList(AnnounceMemberQueryParam announceMemberQueryParam);
}

