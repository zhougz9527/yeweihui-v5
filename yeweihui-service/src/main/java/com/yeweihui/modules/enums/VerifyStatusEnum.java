package com.yeweihui.modules.enums;

/**
 * 审核类型 WAIT_VERIFY 待审核 VERIFIED 已审核 REJECT 未通过 CANCEL 取消申请 RUNNING 活动中
 * Created by Cutie on 2016/7/29.
 * 待审核 -- 已审核 -- 活动中
 * 待审核 -- 未通过
 * 待审核 -- 已审核
 * 待审核 -- 取消申请 -- （重新提交） -- 待审核
 */
public enum VerifyStatusEnum {
    WAIT_VERIFY("待审核","WAIT_VERIFY"),
    VERIFIED("已审核","VERIFIED"),
    REJECT("未通过","REJECT"),
    CANCEL("取消申请","CANCEL"),
    RUNNING("活动中", "RUNNING"),
    ;

    private String name;
    private String code;

    private VerifyStatusEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (VerifyStatusEnum ex: VerifyStatusEnum.values()) {
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
        for (VerifyStatusEnum ex: VerifyStatusEnum.values()) {
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
