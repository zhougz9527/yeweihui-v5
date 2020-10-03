package com.yeweihui.modules.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.modules.user.entity.MpUserEntity;
import com.yeweihui.modules.user.entity.UserEntity;

import java.io.IOException;

public interface MpUserService extends IService<MpUserEntity> {

    void fetchPublicUserInfo();

    void saveOpenIdAndUnionId(String openId, String unionId, Long id);

    void supplementOpenId(String openId, String unionId);

    void insertIgnoreDuplicated(MpUserEntity mpUserEntity);

    void pushTemplateMessage(JSONObject data, Long uid, String type);
}
