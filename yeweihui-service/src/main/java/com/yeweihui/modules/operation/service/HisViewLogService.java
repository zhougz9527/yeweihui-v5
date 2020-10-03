package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.enums.BizTypeEnum;
import com.yeweihui.modules.operation.entity.HisViewLogEntity;
import com.yeweihui.modules.user.entity.UserEntity;

import java.util.Map;

/**
 * 历史记录查看日志服务
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface HisViewLogService extends IService<HisViewLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    HisViewLogEntity info(Long id, UserEntity userEntity);

    void save(BizTypeEnum bizTypeEnum, UserEntity userEntity, Long referId);
}

