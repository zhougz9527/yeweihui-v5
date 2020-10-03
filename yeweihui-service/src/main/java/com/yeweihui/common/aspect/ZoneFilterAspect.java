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

package com.yeweihui.common.aspect;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.yeweihui.common.annotation.ZoneFilter;
import com.yeweihui.common.exception.RRException;
import com.yeweihui.common.utils.Constant;
import com.yeweihui.modules.division.entity.DivisionManagerEntity;
import com.yeweihui.modules.division.service.*;
import com.yeweihui.modules.sys.service.SysDeptService;
import com.yeweihui.modules.sys.service.SysRoleDeptService;
import com.yeweihui.modules.sys.service.SysUserRoleService;
import com.yeweihui.modules.sys.shiro.ShiroUtils;
import com.yeweihui.modules.user.entity.UserEntity;
import com.yeweihui.modules.user.entity.ZonesEntity;
import com.yeweihui.modules.user.service.*;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据过滤，切面处理类
 *
 * @author Mark sunlightcs@gmail.com
 * @since 3.0.0 2017-09-17
 */
@Aspect
@Component
public class ZoneFilterAspect {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private DivisionManagerService divisionManagerService;
    @Autowired
    ZonesService zonesService;
    @Autowired
    UserService userService;

    private final String DIVISION_MANAGER="行业主管";

    @Pointcut("@annotation(com.yeweihui.common.annotation.ZoneFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if(params != null && params instanceof Map){
            UserEntity user = new UserEntity();
            try{
                user = ShiroUtils.getUserEntity();
            }catch (Exception e){
                Object viewUid = ((Map) params).get("viewUid");
                if (viewUid != null) {
                    user = userService.selectById(viewUid.toString());
                } else {
                    return;
                }
            }

            // 如果是运营用户，给他一个标志位
            if (user.getRoleCode().startsWith("13")) {
                Map map = (Map)params;
                map.put(Constant.OPERATE_MANAGER, true);
            }

            //如果不是超级管理员，则进行小区id锅炉
            if(user.getId() != Constant.SUPER_ADMIN){
                Map map = (Map)params;
                String roleName = user.getRoleName();
                Long id = user.getId();
                if (DIVISION_MANAGER.equals(roleName)) {
                    List<DivisionManagerEntity> divisionManagerEntities = divisionManagerService.selectList(
                            new EntityWrapper<DivisionManagerEntity>().eq("user_id", id)
                    );
                    List<ZonesEntity> zonesEntities = new ArrayList<>();
                    if (divisionManagerEntities != null) {
                        for (DivisionManagerEntity manager : divisionManagerEntities) {
                            String level = manager.getLevel();
                            Long divisionId = manager.getDivisionId();
                            if ("province".equals(level)) {
                                zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("province_id", divisionId)));
                            } else if ("city".equals(level)) {
                                zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("city_id", divisionId)));
                            } else if ("district".equals(level)) {
                                zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("district_id", divisionId)));
                            } else if ("subdistrict".equals(level)) {
                                zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("subdistrict_id", divisionId)));
                            } else if ("community".equals(level)) {
                                zonesEntities.addAll(zonesService.selectList(new EntityWrapper<ZonesEntity>().eq("community_id", divisionId)));
                            }
                        }
                    }
                    if (zonesEntities.size() != 0) {
                        map.put("authZoneIdList", zonesEntities.stream().map(ZonesEntity::getId).collect(Collectors.toList()));
                        return;
                    }
                }
                map.put("authZoneId", user.getZoneId());
//                map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
            }

            return ;
        }

        throw new RRException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(UserEntity user, JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        ZoneFilter dataFilter = signature.getMethod().getAnnotation(ZoneFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }

        //部门ID列表
        Set<Long> deptIdList = new HashSet<>();

        //用户角色对应的部门ID列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getId());
        if(roleIdList.size() > 0){
            List<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
            deptIdList.addAll(userDeptIdList);
        }

        //用户子部门ID列表
        if(dataFilter.subDept()){
            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            deptIdList.addAll(subDeptIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if(deptIdList.size() > 0){
            sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }

        //没有本部门数据权限，也能查询本人数据
        if(dataFilter.user()){
            if(deptIdList.size() > 0){
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getId());
        }

        sqlFilter.append(")");

        return sqlFilter.toString();
    }
}
