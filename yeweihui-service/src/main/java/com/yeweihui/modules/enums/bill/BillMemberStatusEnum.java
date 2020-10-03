package com.yeweihui.modules.enums.bill;

import com.yeweihui.common.exception.RRException;

/**
 * 状态 0未审批 1已通过 2未通过
 * Created by Cutie on 2016/12/25.
 */
public enum BillMemberStatusEnum {
    未审批("未审批", 0),
    同意("同意", 1),
    不同意("不同意", 2),
    ;

    private String name;
    private int code;

    private BillMemberStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (BillMemberStatusEnum ex: BillMemberStatusEnum.values()) {
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
        for (BillMemberStatusEnum ex: BillMemberStatusEnum.values()) {
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
