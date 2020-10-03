package com.yeweihui.modules.enums;

/**
 * 用户类型（游客 GUEST、普通用户 NORMAL_USER、业委会达人 DQ_EXPERT、业委会歌手 DQ_SINGER、业委会号 DQ_OFFICIAL_ACCOUNT、管理员 ADMIN）
 * Created by Cutie on 2016/12/22.
 */
public enum UserTypeEnum {
    VIRTUAL_USER("虚拟用户","VIRTUAL_USER"),
    GUEST("游客","GUEST"),
    NORMAL_USER("普通用户","NORMAL_USER"),
    DQ_EXPERT("业委会达人","DQ_EXPERT"),
    DQ_SINGER("业委会歌手","DQ_SINGER"),
    DQ_OFFICIAL_ACCOUNT("业委会号","DQ_OFFICIAL_ACCOUNT"),
    ADMIN("管理员","ADMIN"),
    ;

    private String name;
    private String code;

    private UserTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (UserTypeEnum ex: UserTypeEnum.values()) {
            if (ex.getCode()==code) {
                return ex.getName();
            }
        }
        return null;
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static String getCode(String name) {
        for (UserTypeEnum ex: UserTypeEnum.values()) {
            if (ex.getName().equals(name)) {
                return ex.getCode();
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
