package com.yeweihui.modules.enums.bill;

import com.yeweihui.common.exception.RRException;

/**
 * 报销类型 1小额主任审批 2大额业委会审批
 * Created by Cutie on 2016/12/25.
 */
public enum BillTypeEnum {
    小额主任审批("小额主任审批", 1),
    大额业委会审批("大额业委会审批", 2),
    ;

    private String name;
    private int code;

    private BillTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (BillTypeEnum ex: BillTypeEnum.values()) {
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
        for (BillTypeEnum ex: BillTypeEnum.values()) {
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
