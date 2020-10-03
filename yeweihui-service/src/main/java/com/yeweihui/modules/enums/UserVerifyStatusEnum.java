package com.yeweihui.modules.enums;

/**
 * 用户审核状态 0审核中 1审核通过 2撤销 3未通过
 * Created by Cutie on 2016/7/29.
 */
public enum UserVerifyStatusEnum {
    审核中("审核中",0),
    审核通过("审核通过",1),
    撤销("撤销",2),
    未通过("未通过",3),
    ;

    private String name;
    private int code;

    private UserVerifyStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (UserVerifyStatusEnum ex: UserVerifyStatusEnum.values()) {
            if (ex.getCode()==code) {
                return ex.getName();
            }
        }
        return null;
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static int getCode(String name) {
        for (UserVerifyStatusEnum ex: UserVerifyStatusEnum.values()) {
            if (ex.getName().equals(name)) {
                return ex.getCode();
            }
        }
        return -1;
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
