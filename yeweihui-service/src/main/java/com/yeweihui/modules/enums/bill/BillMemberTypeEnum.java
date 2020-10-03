package com.yeweihui.modules.enums.bill;

import com.yeweihui.common.exception.RRException;

/**
 * 类型 1表决 2抄送
 * Created by Cutie on 2016/12/25.
 */
public enum BillMemberTypeEnum {
    /** 审批 */
    审批("审批", 1),
    /** 抄送 */
    抄送("抄送", 2),
    ;

    private String name;
    private int code;

    private BillMemberTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (BillMemberTypeEnum ex: BillMemberTypeEnum.values()) {
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
        for (BillMemberTypeEnum ex: BillMemberTypeEnum.values()) {
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
