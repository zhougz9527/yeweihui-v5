package com.yeweihui.modules;

import cn.hutool.core.util.RandomUtil;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.RedisUtils;
import com.yeweihui.third.sms.SendSmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commonService")
public class CommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    SendSmsUtils sendSmsUtils;

    /**
     * 发送验证码
     * @param phone
     */
    public void sendVerifyCode(String phone) {
        String verifyCode = RandomUtil.randomString("0123456789", 6);
//        redisUtils.set(phone, verifyCode, 300);
        redisUtils.set(phone, verifyCode, 3600);
//        smsUtils.sendVerifySms(phone, verifyCode);
        logger.info("发送验证码: phone --> " + phone + ", verifyCode --> " + verifyCode);
        sendSmsUtils.sendSmsByTem(phone, "SMS_175574332", "{\"verifyCode\":\""+ verifyCode +"\"}");
    }

    /**
     * 检测验证码
     * @param phone
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(String phone, String verifyCode){
        logger.info("校验验证码: phone --> " + phone + ", verifyCode --> " + verifyCode);
        if (!redisUtils.hasKey(phone)){
            throw new RRException("验证码过期或验证码不存在", -1);
        }
        if (!redisUtils.get(phone).equals(verifyCode)){
            throw new RRException("验证码错误", -2);
        }
        return true;
    }


    /**
     * * 根据模板id给指定的手机号发送短信
     * 通知管理员进行认证模版：SMS_191818114
     * 通知业主认证通过模版：SMS_191818116
     * 通知业主认证失败模版：SMS_191833074
     *
     * 通知业主投票还有24小时结束模版：SMS_194051130
     * @param phone
     * @param templateCode
     * @return
     */
    public boolean sendMsg(String phone, String templateCode) {
        sendSmsUtils.sendSmsNoParam(phone, templateCode);
        return true;
    }



}
