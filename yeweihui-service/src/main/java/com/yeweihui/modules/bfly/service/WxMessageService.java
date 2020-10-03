package com.yeweihui.modules.bfly.service;

import com.alibaba.fastjson.JSONObject;

public interface WxMessageService {

    void pushMessage(JSONObject jsonObject);

    void getPublicOpenIds();

}
