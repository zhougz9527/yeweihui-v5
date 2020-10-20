package com.yeweihui.modules.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yeweihui.common.config.wx.WxMaProperties;
import com.yeweihui.modules.user.dao.MpUserDao;
import com.yeweihui.modules.user.entity.MpUserEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.MessageService;
import com.yeweihui.modules.user.service.MpUserService;
import com.yeweihui.modules.user.service.UserService;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("mpUserService")
public class MpUserServiceImpl extends ServiceImpl<MpUserDao, MpUserEntity> implements MpUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WxMaProperties wxMaProperties;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    private String publicAppId = "wx76a3568799db3640";

    private String publicSecret = "7eda5b694d93cf2e5f8595bc10856581";

    @Override
    @Transactional
    public void fetchPublicUserInfo() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/token").newBuilder();
        urlBuilder.addQueryParameter("grant_type", "client_credential");
        urlBuilder.addQueryParameter("appid", publicAppId);
        urlBuilder.addQueryParameter("secret", publicSecret);
        Request request = new Request.Builder().url(urlBuilder.build()).get().build();
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            JSONObject obj = JSONObject.parseObject(body);
            String accessToken = obj.getString("access_token");
            urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/user/get").newBuilder();
            urlBuilder.addQueryParameter("access_token", accessToken);
            request = new Request.Builder().url(urlBuilder.build()).get().build();
            Response response2 = client.newCall(request).execute();
            body = response2.body().string();
            obj = JSONObject.parseObject(body);
            JSONArray openIds = obj.getJSONObject("data").getJSONArray("openid");
            List<MpUserEntity> mpUserEntities = new ArrayList<>();
            for (Object openId: openIds) {
                urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/user/info").newBuilder();
                urlBuilder.addQueryParameter("access_token", accessToken);
                urlBuilder.addQueryParameter("openid", openId.toString());
                urlBuilder.addQueryParameter("lang", "zh_CN");
                request = new Request.Builder().url(urlBuilder.build()).get().build();
                Response response3 = client.newCall(request).execute();
                obj = JSONObject.parseObject(response3.body().string());
                MpUserEntity mpUserEntity = new MpUserEntity();
                String unionId = obj.getString("unionid");
                if (unionId != null) {
                    mpUserEntity.setUnionId(unionId);
                    mpUserEntity.setOpenidPublic(openId.toString());
                    this.baseMapper.InsertIgnoreDuplicate(mpUserEntity);
                }
            }

        } catch (Exception e) {
            logger.error("公众号保存用户信息出错", e);
        }
    }

    @Override
    @Transactional
    public void saveOpenIdAndUnionId(String openId, String unionId, Long id) {
        MpUserEntity mpUserEntity = new MpUserEntity();
        mpUserEntity.setUnionId(unionId);
        mpUserEntity.setUid(id);
        mpUserEntity.setOpenidMp(openId);
        this.baseMapper.updateById(mpUserEntity);
    }

    @Override
    @Transactional
    public void supplementOpenId(String openId, String unionId) {
        logger.info("openId: {}, unionId: {}", openId, unionId);
        UserEntity userEntity = userService.getByOpenId(openId);
        if (userEntity == null) {
            return;
        } else {
            MpUserEntity mpUserEntity = new MpUserEntity();
            mpUserEntity.setUnionId(unionId);
            mpUserEntity.setUid(userEntity.getId());
            mpUserEntity.setOpenidMp(openId);
            this.baseMapper.updateById(mpUserEntity);
        }
    }

    @Override
    @Transactional
    public void insertIgnoreDuplicated(MpUserEntity mpUserEntity) {
        this.baseMapper.InsertIgnoreDuplicate(mpUserEntity);
    }

    @Override
    @Transactional
    public void pushTemplateMessage(JSONObject data, Long uid, String type) {
//        OkHttpClient client = new OkHttpClient();
//        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/token").newBuilder();
//        urlBuilder.addQueryParameter("grant_type", "client_credential");
//        urlBuilder.addQueryParameter("appid", publicAppId);
//        urlBuilder.addQueryParameter("secret", publicSecret);
//        Request request = new Request.Builder().url(urlBuilder.build()).get().build();
//        try (Response response = client.newCall(request).execute()) {
//            String body = response.body().string();
//            logger.info("request access_token response result: " + body);
//            JSONObject obj = JSONObject.parseObject(body);
//            String accessToken = obj.getString("access_token");
//            logger.info("access_token :" + accessToken);
//            urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/message/template/send").newBuilder();
//            urlBuilder.addQueryParameter("access_token", accessToken);
//
//            RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), data.toJSONString());
//            request = new Request.Builder().url(urlBuilder.build()).post(requestBody).build();
//            Response response2 = client.newCall(request).execute();
//            String body2 = response2.body().string();
//            logger.info("request body2 response: " + body2);
//            JSONObject obj2 = JSONObject.parseObject(body2);
//            logger.info("request 模板消息 response json:" + obj2);
//            if (obj2.getInteger("errcode") != 0) {
//                throw new Exception();
//            }
//            MessageEntity messageEntity = new MessageEntity();
//            messageEntity.setUid(uid);
//            messageEntity.setMessage(data.toJSONString());
//            messageEntity.setType(type);
//            messageService.insert(messageEntity);
//
//        } catch (Exception e) {
//            logger.error("推送模板消息失败", e);
//            throw new RRException("推送模板消息失败, 请确认是否关注【蝴蝶居】公众号");
//        }

    }

}
