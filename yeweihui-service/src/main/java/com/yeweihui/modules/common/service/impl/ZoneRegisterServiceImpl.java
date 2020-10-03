package com.yeweihui.modules.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yeweihui.common.utils.BeanUtil;
import com.yeweihui.common.utils.StrUtils;
import com.yeweihui.modules.enums.PlatEnum;
import com.yeweihui.modules.vo.bo.SendSmsByTemBO;
import com.yeweihui.third.sms.SendSmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.utils.PageUtils;
import com.yeweihui.common.utils.Query;

import com.yeweihui.modules.common.dao.ZoneRegisterDao;
import com.yeweihui.modules.common.entity.ZoneRegisterEntity;
import com.yeweihui.modules.common.service.ZoneRegisterService;


@Service("zoneRegisterService")
public class ZoneRegisterServiceImpl extends ServiceImpl<ZoneRegisterDao, ZoneRegisterEntity> implements ZoneRegisterService {

    @Autowired
    SendSmsUtils sendSmsUtils;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ZoneRegisterEntity> page = this.selectPage(
                new Query<ZoneRegisterEntity>(params).getPage(),
                new EntityWrapper<ZoneRegisterEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void save(ZoneRegisterEntity zoneRegister) {
        String bdPhone = "18072955121";
        SendSmsByTemBO sendSmsByTemBO = new SendSmsByTemBO();
        sendSmsByTemBO.setAddress(zoneRegister.getAddress());
        sendSmsByTemBO.setName(zoneRegister.getName());
        sendSmsByTemBO.setPhone(zoneRegister.getPhone());
        sendSmsByTemBO.setZoneName(zoneRegister.getZoneName());
        sendSmsByTemBO.setPlat(PlatEnum.getName(zoneRegister.getPlat()));
        if(zoneRegister.getPlat().equals(PlatEnum.公众号.getCode())){
            //发送公众号短信
            //SMS_175539736  ${plat}收到新客户的开通申请，请尽快联系。客户姓名：${name}；电话：${phone}；小区名字：${zoneName}；小区地址：${address}。
            sendSmsUtils.sendSmsByTem(bdPhone, "SMS_175539736", StrUtils.toJson(sendSmsByTemBO));
        }else if(zoneRegister.getPlat().equals(PlatEnum.官网.getCode())){
            //发送官网短信
            sendSmsUtils.sendSmsByTem(bdPhone, "SMS_175539736", StrUtils.toJson(sendSmsByTemBO));
        }
        this.insert(zoneRegister);
    }

}
