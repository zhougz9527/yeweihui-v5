package com.yeweihui.modules.enums;

/**
 * 认证类型 身份证 IDENTITY_CARD，营业执照 BUSINESS_LICENSE
 * Created by Cutie on 2016/7/29.
 */
public enum IdentityTypeEnum {
    IDENTITY_CARD("身份证","IDENTITY_CARD"),
    BUSINESS_LICENSE("营业执照","BUSINESS_LICENSE"),
    ;

    private String name;
    private String code;

    private IdentityTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (IdentityTypeEnum ex: IdentityTypeEnum.values()) {
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
        for (IdentityTypeEnum ex: IdentityTypeEnum.values()) {
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
