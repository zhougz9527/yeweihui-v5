package com.yeweihui.modules.enums.request;

import com.yeweihui.common.exception.RRException;

/**
 * 审核状态 0审核中 1审核通过 2撤销 3未通过
 * Created by Cutie on 2016/12/25.
 */
public enum RequestStatusEnum {
    审核中("审核中", 0),
    审核通过("审核通过", 1),
    撤销("撤销", 2),
    未通过("未通过", 3),
    ;

    private String name;
    private int code;

    private RequestStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (RequestStatusEnum ex: RequestStatusEnum.values()) {
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
        for (RequestStatusEnum ex: RequestStatusEnum.values()) {
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
