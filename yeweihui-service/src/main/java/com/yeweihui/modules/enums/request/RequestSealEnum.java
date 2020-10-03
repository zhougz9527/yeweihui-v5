package com.yeweihui.modules.enums.request;

import com.yeweihui.common.exception.RRException;

/**
 * 印章 1业主大会 2业委会 3财务专用章 4其他
 * new 印章 1业委会公章 2业主大会公章 3财务专用章 4其他    ["业委会公章", "业主大会公章", "财务专用章", "其他"]
 * Created by Cutie on 2016/12/25.
 */
public enum RequestSealEnum {
    业委会公章("业委会公章", 1),
    业主大会公章("业主大会公章", 2),
    财务专用章("财务专用章", 3),
    其他("其他", 4),
    ;

    private String name;
    private int code;

    private RequestSealEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (RequestSealEnum ex: RequestSealEnum.values()) {
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
        for (RequestSealEnum ex: RequestSealEnum.values()) {
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
