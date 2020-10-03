package com.yeweihui.modules.accounts.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.accounts.entity.AccountsFinancialinformEntity;
import com.yeweihui.modules.vo.query.ReceiptsAndDisbursementsParam;

import java.util.Date;
import java.util.Map;

/**
 * Created by zss86 on 2020/8/21.
 */
public interface ReportsService extends IService<AccountsFinancialinformEntity> {
    Map blance(Long zoneId);
    Map monthOrYearStatistical(Long zoneId,Date statisticalDate,boolean isYear);
    Map detail(Long zoneId, Date statisticalDate,boolean isYear);
    Map selectSubjectDetail(ReceiptsAndDisbursementsParam receiptsAndDisbursementsParam);
    Map recorded(Long zoneId, Date statisticalDate);
    Map getFileInfoVO(Date statisticalDate);
    }
