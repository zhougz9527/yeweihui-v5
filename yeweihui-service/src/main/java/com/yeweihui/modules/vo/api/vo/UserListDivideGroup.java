package com.yeweihui.modules.vo.api.vo;

import com.yeweihui.modules.user.entity.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserListDivideGroup {
    //角色组别名称
    private String groupName;
    private List<UserEntity> userEntityList;
}
