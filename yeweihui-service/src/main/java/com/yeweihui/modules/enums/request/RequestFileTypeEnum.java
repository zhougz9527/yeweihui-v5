package com.yeweihui.modules.enums.request;

import com.yeweihui.common.exception.RRException;

/**
 * 用章类别 1合同 2公告 3发函 4其他
 * Created by Cutie on 2016/12/25.
 */
public enum RequestFileTypeEnum {
    合同("合同", 1),
    公告("公告", 2),
    发函("发函", 3),
    其他("其他", 4),
    ;

    private String name;
    private int code;

    private RequestFileTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (RequestFileTypeEnum ex: RequestFileTypeEnum.values()) {
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
        for (RequestFileTypeEnum ex: RequestFileTypeEnum.values()) {
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
