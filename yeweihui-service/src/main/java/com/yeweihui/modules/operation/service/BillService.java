package com.yeweihui.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.operation.entity.BillEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.vo.api.form.BillVerifyForm;
import com.yeweihui.modules.vo.query.BillQueryParam;

import java.util.Map;

/**
 * 报销表
 *
 * @author cutie
 * @email cutieagain@gmail.com
 * @date 2019-09-10 18:25:21
 */
public interface BillService extends IService<BillEntity> {

    PageUtils queryPage(Map<String, Object> params);

    BillEntity info(Long id, UserEntity userEntity);

    void save(BillEntity bill);

    void update(BillEntity bill);

    int getCount(BillQueryParam billQueryParam);

    /**
     * 通过拒绝报销审批
     * @param billVerifyForm
     */
    void verify(BillVerifyForm billVerifyForm);
}

