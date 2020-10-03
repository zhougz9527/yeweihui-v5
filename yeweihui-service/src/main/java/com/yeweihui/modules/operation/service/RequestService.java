package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.RequestEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.request.RequestCancelForm;
import com.yeweihui.modules.vo.api.form.request.RequestVerifyForm;
import com.yeweihui.modules.vo.query.RequestQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 用章申请表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface RequestService extends IService<RequestEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void save(RequestEntity request);

    void update(RequestEntity request);

    RequestEntity info(Long id, UserEntity userEntity);

    int getCount(RequestQueryParam requestQueryParam);

    /**
     * 审核通过/拒绝
     * @param requestVerifyForm
     */
    void verify(RequestVerifyForm requestVerifyForm);

    /**
     * 获取出超过24小时，审核未完成的默认审核通过；并更新用章状态
     */
    void expireRequest();

    List<RequestEntity> simpleList(RequestQueryParam requestQueryParam);

    /**
     * 审核撤销
     * @param requestCancelForm
     */
    void requestCancel(RequestCancelForm requestCancelForm);
}

