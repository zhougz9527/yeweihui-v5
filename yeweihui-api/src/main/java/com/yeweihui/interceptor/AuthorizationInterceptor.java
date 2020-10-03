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
import com.yeweihui.annotation.LoginUser;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.service.SysRoleMenuService;
import com.yeweihui.modules.user.dao.UserDao;
import com.yeweihui.modules.user.entity.TokenEntity;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.TokenService;
import com.yeweihui.modules.user.service.UserService;
import com.yeweihui.modules.user.service.ZonesService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Permission;
import java.util.Date;
import java.util.List;

/**
 * 权限(Token)验证
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:38
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ZonesService zonesService;
    @Autowired
    private UserService userService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    UserDao userDao;

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
            String token = request.getHeader("token");
            //如果header中不存在token，则从参数中获取token
            if(StringUtils.isBlank(token)){
                token = request.getParameter("token");
            }
            if (token != null) {
                TokenEntity tokenEntity = tokenService.queryByToken(token);
                if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
                    throw new RRException("token失效，请重新登录");
                }
                request.setAttribute(USER_KEY, tokenEntity.getUserId());
            }
            return true;
        }

        //从header中获取token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }

        //token为空
        if(StringUtils.isBlank(token)){
            throw new RRException("token不能为空");
        }

        //查询token信息
        TokenEntity tokenEntity = tokenService.queryByToken(token);
        if(tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()){
            throw new RRException("token失效，请重新登录");
        }

        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, tokenEntity.getUserId());
        UserEntity userEntity = userService.selectById(tokenEntity.getUserId());
        if (userEntity != null){

            RequiresPermissions requiresPermissions;
            if (handler instanceof HandlerMethod) {
                requiresPermissions = ((HandlerMethod) handler).getMethodAnnotation(RequiresPermissions.class);
                if (requiresPermissions == null) {
                    return true;
                }
                List<String> permList = userDao.queryAllPerms(userEntity.getId());
                String[] perms = requiresPermissions.value();

                if (!hasPermission(permList, perms)) {
                    throw new RRException("您无权限操作");
                }
            }
        }

        return true;
    }

    private boolean hasPermission(List<String> permList, String[] perms) {
        for (String perm: perms) {
            boolean contains = false;
            for (String myPerm: permList) {
                if (myPerm != null && myPerm.contains(perm)) {
                    contains = true;
                }
            }
            if (!contains) {
                return false;
            }
        }
        return true;
    }

}
