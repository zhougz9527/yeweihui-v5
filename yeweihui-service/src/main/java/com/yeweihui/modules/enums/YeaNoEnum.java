package com.yeweihui.modules.enums;

import com.yeweihui.common.exception.RRException;

/**
 * 是否
 * Created by Cutie on 2016/12/25.
 */
public enum YeaNoEnum {
    YES("是", 1),
    NO("否", 0)
    ;

    private String name;
    private int code;

    private YeaNoEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (YeaNoEnum ex: YeaNoEnum.values()) {
            if (ex.getCode()==code) {
                return ex.getName();
            }
        }
        throw new RRException("找不到对应名称的枚举name");
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static int getCode(String name) {
        for (YeaNoEnum ex: YeaNoEnum.values()) {
            if (ex.getName().equals(name)) {
                return ex.getCode();
            }
        }
        throw new RRException("找不到对应名称的枚举code");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
