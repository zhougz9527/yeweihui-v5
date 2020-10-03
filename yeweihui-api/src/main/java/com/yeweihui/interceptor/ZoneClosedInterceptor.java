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

package com.yeweihui.interceptor;


import cn.hutool.core.date.DateUtil;
import com.yeweihui.annotation.Login;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.modules.user.entity.TokenEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.TokenService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 关停小区验证
 * @date 2017-03-23 15:38
 */
@Component
public class ZoneClosedInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ZonesService zonesService;
    @Autowired
    private UserService userService;

    public static final String USER_KEY = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Login annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Login.class);
        }else{
            return true;
        }

        if(annotation == null){
            return true;
        }

        Object userId = request.getAttribute(USER_KEY);
        if (userId == null) {
            throw new RRException("请先登录");
        }
        UserEntity userEntity = userService.selectById((Long)userId);
        if (userEntity != null){
            ZonesEntity zonesEntity = zonesService.selectById(userEntity.getZoneId());
            if (null != zonesEntity){
                if (zonesEntity.getId() == 0) {
                    return true;
                }
                if (zonesEntity.getStatus() == 0) {
                    throw new RRException("您的小区已经被关停");
                }
                if (zonesEntity.getEnableUseTime().compareTo(DateUtil.tomorrow())<0){
                    throw new RRException("小区超过使用期，无法使用");
                }
            }
        }
        return true;

    }

}
