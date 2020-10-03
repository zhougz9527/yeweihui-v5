package com.yeweihui.modules.enums;

/**
 * 审核类型
 * Created by Cutie on 2016/12/26.
 */
public enum SexEnum {
    MALE("男","MALE"),
    FEMALE("女","FEMALE"),
    UNKNOWN("未知","UNKNOWN"),
    ;

    private String name;
    private String code;

    private SexEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (SexEnum ex: SexEnum.values()) {
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
        for (SexEnum ex: SexEnum.values()) {
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
