package com.yeweihui.modules.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.modules.user.entity.MessageEntity;

import java.util.List;
import java.util.Map;

public interface MessageService extends IService<MessageEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<MessageEntity> simpleList(MessageEntity messageEntity);
}
