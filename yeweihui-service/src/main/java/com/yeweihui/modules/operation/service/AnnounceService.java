package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.AnnounceEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.announce.AnnounceOpeForm;
import com.yeweihui.modules.vo.query.AnnounceQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 公示记录
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface AnnounceService extends IService<AnnounceEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AnnounceEntity info(Long id, UserEntity userEntity);

    void save(AnnounceEntity paper);

    void update(AnnounceEntity paper);

    List<AnnounceEntity> simpleList(AnnounceQueryParam announceQueryParam);

    void finish(AnnounceOpeForm announceOpeForm);

    void expireAnnounce();
}

