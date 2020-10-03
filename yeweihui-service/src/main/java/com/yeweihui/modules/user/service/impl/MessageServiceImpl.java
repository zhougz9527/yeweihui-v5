package com.yeweihui.modules.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.annotation.DataFilter;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;
import com.yeweihui.modules.user.dao.MessageDao;
import com.yeweihui.modules.user.entity.MessageEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.MessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long uid = (Long)params.get("uid");
        String type = (String)params.get("type");
        String message = (String)params.get("message");

        Page<MessageEntity> page = this.selectPage(
                new Query<MessageEntity>(params).getPage(),
                new EntityWrapper<MessageEntity>()
                        .eq(uid != null , "uid", uid)
        );

        return new PageUtils(page);
    }

    @Override
    public List<MessageEntity> simpleList(MessageEntity messageEntity) {
        return this.selectList(new EntityWrapper<MessageEntity>()
                .eq(messageEntity.getUid() != null, "uid", messageEntity.getUid())
                .eq(messageEntity.getType() !=null, "type", messageEntity.getType()));
    }
}
