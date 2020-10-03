/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.yeweihui.modules.sys.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.R;
import com.yeweihui.common.validator.ValidatorUtils;
import com.yeweihui.modules.CommonService;
import com.yeweihui.modules.sys.service.SysRoleService;
import com.yeweihui.modules.sys.shiro.NoPasswordToken;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import com.yeweihui.modules.vo.api.form.user.SmsLoginForm;
import com.yeweihui.modules.vo.api.vo.LoginVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;

	@Autowired
	UserService userService;

	@Autowired
	ZonesService zonesService;

	@Autowired
	SysRoleService sysRoleService;

	@Autowired
	CommonService commonService;
	
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}
	
	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String username, String password, String captcha) {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}

		UserEntity userEntity = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));
		if (null != userEntity){
			ZonesEntity zonesEntity = zonesService.selectById(userEntity.getZoneId());
			if (null != zonesEntity){
				//今天日期大于有效日志
				if (null != zonesEntity.getEnableUseTime() && zonesEntity.getEnableUseTime().compareTo(DateUtil.tomorrow())<0){
					throw new RRException("小区超过使用期，无法使用");
				}
			}
		}

		try{
			Subject subject = ShiroUtils.getSubject();
			NoPasswordToken token = new NoPasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
	    
		return R.ok();
	}

	@ResponseBody
	@RequestMapping(value = "/sys/smsLogin", method = RequestMethod.POST)
	public R smsLogin(String mobile, String verifyCode, String captcha){
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}

		UserEntity userEntity = userService.selectOne(new EntityWrapper<UserEntity>().eq("mobile", mobile));
		if (null != userEntity){
			ZonesEntity zonesEntity = zonesService.selectById(userEntity.getZoneId());
			if (null != zonesEntity){
				//今天日期大于有效日志
				if (null != zonesEntity.getEnableUseTime() && zonesEntity.getEnableUseTime().compareTo(DateUtil.tomorrow())<0){
					throw new RRException("小区超过使用期，无法使用");
				}
			}
		}

		SmsLoginForm smsLoginForm = new SmsLoginForm();
		smsLoginForm.setMobile(mobile);
		smsLoginForm.setVerifyCode(verifyCode);
		ValidatorUtils.validateEntity(smsLoginForm);
		LoginVO loginResult = userService.smsLogin(smsLoginForm);

		Subject subject = SecurityUtils.getSubject();
		NoPasswordToken token = new NoPasswordToken(userEntity.getUsername());
		subject.login(token);
		return R.ok().put("loginResult", loginResult);
	}

	@ResponseBody
	@RequestMapping(value = "/sys/sendVerifyCode", method = RequestMethod.POST)
	public R sendVerifyCode(String phone){
		commonService.sendVerifyCode(phone);
		return R.ok();
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}
	
}
