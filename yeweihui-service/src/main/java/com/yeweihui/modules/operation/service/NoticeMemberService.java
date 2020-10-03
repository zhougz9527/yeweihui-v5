package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.operation.entity.NoticeMemberEntity;
import com.yeweihui.modules.vo.query.NoticeMemberQueryParam;

import java.util.List;

public interface NoticeMemberService extends IService<NoticeMemberEntity> {


    List<NoticeMemberEntity> simpleList(NoticeMemberQueryParam noticeMemberQueryParam);

}
