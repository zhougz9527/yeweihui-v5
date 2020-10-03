package com.yeweihui.modules.operation.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.yeweihui.modules.operation.entity.AnnounceMemberEntity;
import com.yeweihui.modules.vo.query.AnnounceMemberQueryParam;

import java.util.List;

/**
 * 公示记录签收表
 * 
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface AnnounceMemberDao extends BaseMapper<AnnounceMemberEntity> {


    List<AnnounceMemberEntity> simpleList(AnnounceMemberQueryParam announceMemberQueryParam);
}
