package com.yeweihui.modules.enums;

public enum LoginTypeEnum {

    PASSWORD("PASSWORD", "0"),
    NOPASSWORD("NOPASSWORD","1");

    private String name;
    private String code;

    private LoginTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (FileTypeEnum ex: FileTypeEnum.values()) {
            if (ex.getCode().equals(code)) {
                return ex.getName();
            }
        }
        return null;
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static String getCode(String name) {
        for (FileTypeEnum ex: FileTypeEnum.values()) {
            if (ex.getName().equals(name)) {
                return ex.getCode();
            }
        }
        return "";
    }

    /**
     * 根据code获取对应的枚举
     * @param code
     * @return
     */
    public static LoginTypeEnum getEnumByCode(String code){
        for(LoginTypeEnum bizTypeEnum : LoginTypeEnum.values()){
            if(code.equals(bizTypeEnum.getCode())){
                return bizTypeEnum;
            }
        }
        return null;
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
