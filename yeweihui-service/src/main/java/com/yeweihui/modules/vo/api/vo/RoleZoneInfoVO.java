package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.sys.entity.SysRoleEntity;
import com.yeweihui.modules.sys.entity.SysUserRoleEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleZoneInfoVO {
    // 用户角色
    private List<SysRoleEntity> sysRoleEntityList;
    // 小区开通日期
    private Date registerTime;
    // 服务截止日期
    private Date enableUseTime;
}
