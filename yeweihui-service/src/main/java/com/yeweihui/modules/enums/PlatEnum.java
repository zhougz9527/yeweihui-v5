package com.yeweihui.modules.enums;

/**
 * 平台类型
 * Created by Cutie on 2019/1/7.
 * 1官网 2公众号
 */
public enum PlatEnum {
    官网("官网",1),
    公众号("公众号",2),
    ;

    private String name;
    private int code;

    private PlatEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (PlatEnum ex: PlatEnum.values()) {
            if (ex.getCode() == code) {
                return ex.getName();
            }
        }
        return null;
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static int getCode(String name) {
        for (PlatEnum ex: PlatEnum.values()) {
            if (ex.getName().equals(name)) {
                return ex.getCode();
            }
        }
        return 0;
    }

    /**
     * 根据code获取对应的枚举
     * @param code
     * @return
     */
    public static PlatEnum getEnumByCode(String code){
        for(PlatEnum bizTypeEnum : PlatEnum.values()){
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
