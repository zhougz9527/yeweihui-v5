package com.yeweihui.modules.enums;

import com.yeweihui.common.exception.RRException;

/**
 * 粉丝关注状态
 * 状态 是否关注 1关注 0取消关注 -1拉黑
 * Created by Cutie on 2016/12/6.
 */
public enum FansStatusEnum {
    FOLLOW("关注", 1),
    UN_FOLLOW("取消关注", 0),
    GO_BLACK("拉黑", -1),
    ;

    private String name;
    private int code;

    private FansStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (FansStatusEnum ex: FansStatusEnum.values()) {
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
        for (FansStatusEnum ex: FansStatusEnum.values()) {
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
