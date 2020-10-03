package com.yeweihui.controller.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.yeweihui.common.config.wx.WxMaConfiguration;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.utils.StrUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.service.MpUserService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.vo.api.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user/{appid}")
@Api(tags="微信小程序用户接口")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    MpUserService mpUserService;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    @ApiOperation("登陆接口")
    public R login(@PathVariable String appid, String code) {
        if (StringUtils.isBlank(code)) {
//            return "empty jscode";
            throw new RRException("empty jscode");
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
//            return StrUtils.toJson(session);
            //根据openId登录
            LoginVO loginResult = userService.loginByOpenId(session.getOpenid());
//            LoginVO loginResult = userService.loginByOpenIdAndPhone(session.getOpenid(), phone);
            loginResult.setSession(session);
            mpUserService.supplementOpenId(session.getOpenid(), session.getUnionid());

            return R.ok().put("loginResult", loginResult);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            throw new RRException(e.toString());
//            return e.toString();
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    @ApiOperation("获取用户信息接口")
    public R info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv, String phone) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new RRException("user check failed");
//            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        //查询是否该openId已经存在
        UserEntity userEntity = userService.getByOpenId(userInfo.getOpenId());
        if (null == userEntity){
            //如果不存在的话进行注册
            //根据微信用户信息保存微信用户信息，并注册用户（默认密码123456）
            userEntity = userService.registerWx(userInfo, phone);
            mpUserService.saveOpenIdAndUnionId(userInfo.getOpenId(), userInfo.getUnionId(), userEntity.getId());
        }else{
            //更新下？暂时不需要
        }

        return R.ok().put("userInfo", userInfo)
                .put("userEntity", userEntity);
//        return StrUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    @ApiOperation("获取用户绑定手机号信息")
    public R phone(@PathVariable String appid, String sessionKey, String signature,
                   String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            throw new RRException("user check failed");
//            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return R.ok().put("phoneNoInfo", phoneNoInfo);
//        return StrUtils.toJson(phoneNoInfo);
    }

}
