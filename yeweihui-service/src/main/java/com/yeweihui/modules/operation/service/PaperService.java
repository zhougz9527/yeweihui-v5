package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.PaperEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.paper.PaperSignForm;
import com.yeweihui.modules.vo.query.PaperQueryParam;

import java.util.Map;

/**
 * 来往函件表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface PaperService extends IService<PaperEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PaperEntity info(Long id, UserEntity userEntity);

    void save(PaperEntity paper);

    void update(PaperEntity paper);

    int getCount(PaperQueryParam paperQueryParam);

    void paperSign(PaperSignForm paperSignForm);
}

