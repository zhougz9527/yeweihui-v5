package com.yeweihui.modules.common.service;

import com.yeweihui.modules.vo.api.vo.MainPage1VO;
import com.yeweihui.modules.vo.query.UserIdQueryParam;

public interface MainPageService {
    /**
     * 首页头部信息1（小区信息，待我用章审批数量，待我事项表决数量，网上发函数量，费用报销数量）
     * @param userIdQueryParam
     * @return
     */
    MainPage1VO mainPage1(UserIdQueryParam userIdQueryParam);
}
