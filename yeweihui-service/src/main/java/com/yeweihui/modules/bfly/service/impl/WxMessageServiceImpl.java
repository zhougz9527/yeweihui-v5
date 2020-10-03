package com.yeweihui.modules.bfly.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.modules.bfly.entity.BflyUser;
import com.yeweihui.modules.bfly.service.BflyUserService;
import com.yeweihui.modules.bfly.service.WxMessageService;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("wxMessageService")
public class WxMessageServiceImpl implements WxMessageService {

    @Resource
    BflyUserService bflyUserService;

    private String publicAppId = "wx76a3568799db3640";// 公众号的openid
    private String publicSecret = "7eda5b694d93cf2e5f8595bc10856581";// 公众号的secret

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 小程序消息推送
     * @param data
     */
    @Override
    public void pushMessage(JSONObject data) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder builder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/token").newBuilder();
        builder.addQueryParameter("grant_type", "client_credential");
        builder.addQueryParameter("appid", publicAppId);
        builder.addQueryParameter("secret", publicSecret);
        Request request = new Request.Builder().url(builder.build()).get().build();
        try {
            // 获取access_token
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            logger.info("body -> " + body);
            JSONObject obj = JSONObject.parseObject(body);
            String accessToken = obj.getString("access_token");
            logger.info("access_token -> " + accessToken);
            builder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/message/template/send").newBuilder();
            builder.addQueryParameter("access_token", accessToken);
            //发送模板
            RequestBody requestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), data.toJSONString());
            request = new Request.Builder().url(builder.build()).post(requestBody).build();
            Response response2 = client.newCall(request).execute();
            obj = JSONObject.parseObject(response2.body().string());
            logger.info(obj.toJSONString());
            if (obj.getInteger("errcode") != 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("推送模板消息失败, error msg --> " + e.getMessage());
//            throw new RRException("推送模板消息失败");
        }


    }

    /**
     * 获取所有用户微信公众号的openid
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getPublicOpenIds() {
        logger.info("获取所有用户的微信公众号的openid...");
        List<BflyUser> bflyUserList = bflyUserService.selectList(new EntityWrapper<BflyUser>().eq("public_open_id", ""));
        if (null == bflyUserList || bflyUserList.size() == 0) {
            return ;
        }
        // 获取access_token
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/token").newBuilder();
        urlBuilder.addQueryParameter("grant_type", "client_credential");
        urlBuilder.addQueryParameter("appid", publicAppId);
        urlBuilder.addQueryParameter("secret", publicSecret);
        Request request = new Request.Builder().url(urlBuilder.build()).get().build();
        try {
            Response response = client.newCall(request).execute();
            String accessTokenBody = response.body().string();
            logger.info("accessTokenBody --> " + accessTokenBody);
            JSONObject obj = JSONObject.parseObject(accessTokenBody);
            String accessToken = obj.getString("access_token");
            logger.info("access_token --> " + accessToken);
            // 微信公众号用户的openId列表
            urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/user/get").newBuilder();
            urlBuilder.addQueryParameter("access_token", accessToken);
            request = new Request.Builder().url(urlBuilder.build()).get().build();
            Response usersResponse = client.newCall(request).execute();
            String usersBody = usersResponse.body().string();
            logger.info("usersBody --> " + usersBody);
            obj = JSONObject.parseObject(usersBody);
            JSONArray openIds = obj.getJSONObject("data").getJSONArray("openid");// 微信公众号用户的openid列表
            for (Object openId : openIds) {
                urlBuilder = HttpUrl.parse("https://api.weixin.qq.com/cgi-bin/user/info").newBuilder();
                urlBuilder.addQueryParameter("access_token", accessToken);
                urlBuilder.addQueryParameter("openid", openId.toString());
                urlBuilder.addQueryParameter("lang", "zh_CN");
                request = new Request.Builder().url(urlBuilder.build()).get().build();
                Response unionidResponse = client.newCall(request).execute();
                obj = JSONObject.parseObject(unionidResponse.body().string());
                String unionId = obj.getString("unionid");
                BflyUser bflyUser = bflyUserService.selectOne(new EntityWrapper<BflyUser>().eq("union_id", unionId));
                if (null != bflyUser && StringUtils.isEmpty(bflyUser.getPublicOpenId())) {// 用户没有unionid则更新
                    bflyUser.setPublicOpenId(String.valueOf(openId));
                    bflyUserService.updateById(bflyUser);
                }
            }
        } catch (Exception e) {
            logger.error("获取微信公众号用户的openid失败, 错误信息: " + e.getMessage());
        }
    }


}
