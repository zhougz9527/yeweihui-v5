package com.yeweihui.third.getui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

//import com.gexin.rp.sdk.base.IAliasResult;
//import com.gexin.rp.sdk.base.IPushResult;
//import com.gexin.rp.sdk.base.impl.SingleMessage;
//import com.gexin.rp.sdk.base.impl.Target;
//import com.gexin.rp.sdk.base.payload.APNPayload;
//import com.gexin.rp.sdk.exceptions.RequestException;
//import com.gexin.rp.sdk.http.IGtPush;
//import com.gexin.rp.sdk.template.LinkTemplate;
//import com.gexin.rp.sdk.template.NotificationTemplate;
//import com.gexin.rp.sdk.template.TransmissionTemplate;
//import com.gexin.rp.sdk.template.style.AbstractNotifyStyle;
//import com.gexin.rp.sdk.template.style.Style0;

//@Component
public class GetuiUtils {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${getui.appId}")
    private String appId;
    @Value("${getui.appSecret}")
    private String appSecret;
    @Value("${getui.appKey}")
    private String appKey;
    @Value("${getui.masterSecret}")
    private String masterSecret;
    @Value("${getui.host}")
    private String host;

    /**
     * 绑定别名
     * @param alias
     * @param clientId
     */
    public void bindAlias(String alias, String clientId){
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        IAliasResult bindSCid = push.bindAlias(appId, alias, clientId);
//        logger.info("绑定结果:{}, 错误码:{}", bindSCid.getResult(), bindSCid.getErrorMsg());
    }

    /**
     * 单个推送
     * @param alias 使用用户id
     * @param messageInfo
     */
    public void pushToSingle(String alias, String title, String messageInfo){
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        TransmissionTemplate template = transmissionTemplate(title, messageInfo);
//        SingleMessage message = new SingleMessage();
//        message.setOffline(true);
//        // 离线有效时间，单位为毫秒，可选
//        message.setOfflineExpireTime(24 * 3600 * 1000);
//        message.setData(template);
//        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
//        message.setPushNetWorkType(0);
//        Target target = new Target();
//        target.setAppId(appId);
////        target.setClientId(CID);
//        target.setAlias(alias);
//        IPushResult ret = null;
//        try {
//            ret = push.pushMessageToSingle(message, target);
//        } catch (RequestException e) {
//            logger.error("RequestException resend pushMessageToSingle" + e.getMessage(), e);
//            ret = push.pushMessageToSingle(message, target, e.getRequestId());
//        }
//        if (ret != null) {
//            logger.info(ret.getResponse().toString());
//        } else {
//            logger.info("服务器响应异常");
//            throw new RRException("服务器响应异常");
//        }
    }

    /**
     * 普通推送
     * @param title
     * @param messageInfo
     * @return
     */
//    private TransmissionTemplate transmissionTemplate(String title, String messageInfo){
//        /*NotificationTemplate notificationTemplate = new  NotificationTemplate();
//        notificationTemplate.setAppId(appId);
//        notificationTemplate.setAppkey(appKey);
//        notificationTemplate.setTitle(title);
//        notificationTemplate.setText(messageInfo);
//        notificationTemplate.setLogo("push.png");
//        notificationTemplate.setIsRing(true);
//        notificationTemplate.setIsVibrate(true);
//        notificationTemplate.setIsClearable(true);
//        return notificationTemplate;*/
//
//        TransmissionTemplate template = new TransmissionTemplate();
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//        template.setTransmissionContent(messageInfo);
//        template.setTransmissionType(2);
//        APNPayload payload = new APNPayload();
//        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//        payload.setAutoBadge("+1");
//        payload.setContentAvailable(1);
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        payload.addCustomMsg("payload", "payload");
//
//        //简单模式APNPayload.SimpleMsg
////        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(title));
//
//        //字典模式使用APNPayload.DictionaryAlertMsg
//        payload.setAlertMsg(getDictionaryAlertMsg(title, messageInfo));
//
//        //设置语音播报类型，int类型，0.不可用 1.播放body 2.播放自定义文本
////        payload.setVoicePlayType(0);
//        //设置语音播报内容，String类型，非必须参数，用户自定义播放内容，仅在voicePlayMessage=2时生效
//        //注：当"定义类型"=2, "定义内容"为空时则忽略不播放
////        payload.setVoicePlayMessage("定义内容");
//
//        // 添加多媒体资源
////        payload.addMultiMedia(new MultiMedia().setResType(MultiMedia.MediaType.video)
////                .setResUrl("http://ol5mrj259.bkt.clouddn.com/test2.mp4")
////                .setOnlyWifi(true));
//        //需要使用IOS语音推送，请使用VoIPPayload代替APNPayload
//        // VoIPPayload payload = new VoIPPayload();
//        // JSONObject jo = new JSONObject();
//        // jo.put("key1","value1");
//        //         payload.setVoIPPayload(jo.toString());
//        //
//        template.setAPNInfo(payload);
//        return template;
//    }
//
//
//    private LinkTemplate linkTemplateDemo(String title, String messageInfo) {
//        LinkTemplate template = new LinkTemplate();
//        // 设置APPID与APPKEY
//        template.setAppId(appId);
//        template.setAppkey(appKey);
//        // 设置通知栏标题与内容
//        template.setTitle(title);
//        template.setText(messageInfo);
//        // 配置通知栏图标
//        template.setLogo("push.png");
//        // 配置通知栏网络图标，填写图标URL地址
//        template.setLogoUrl("");
//        // 设置通知是否响铃，震动，或者可清除
//        template.setIsRing(true);
//        template.setIsVibrate(true);
//        template.setIsClearable(true);
//        // 设置打开的网址地址
//        template.setUrl("http://www.baidu.com");
//        return template;
//    }
//
//    private NotificationTemplate notificationTemplate(String title, String messageInfo){
//        NotificationTemplate notificationTemplate = new  NotificationTemplate();
//        notificationTemplate.setAppId(appId);
//        notificationTemplate.setAppkey(appKey);
//        notificationTemplate.setTitle(title);
//        notificationTemplate.setText(messageInfo);
//        notificationTemplate.setLogo("push.png");
//        notificationTemplate.setIsRing(true);
//        notificationTemplate.setIsVibrate(true);
//        notificationTemplate.setIsClearable(true);
//
//        APNPayload payload = new APNPayload();
//        //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
//        payload.setAutoBadge("+1");
//        payload.setContentAvailable(1);
//        payload.setSound("default");
//        payload.setCategory("$由客户端定义");
//        payload.addCustomMsg("payload", "payload");
//        payload.setAlertMsg(getDictionaryAlertMsg(title, messageInfo));
//        notificationTemplate.setAPNInfo(payload);
//
//        return notificationTemplate;
//    }
//
//    private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(String title, String body){
//        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
//        alertMsg.setBody(body);
//        alertMsg.setActionLocKey("ActionLockey");
//        alertMsg.setLocKey("LocKey");
//        alertMsg.addLocArg("loc-args");
//        alertMsg.setLaunchImage("launch-image");
//        // iOS8.2以上版本支持
//        alertMsg.setTitle(title);
//        alertMsg.setTitleLocKey("TitleLocKey");
//        alertMsg.addTitleLocArg("TitleLocArg");
//        return alertMsg;
//    }

}
