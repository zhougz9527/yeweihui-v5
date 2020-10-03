package com.yeweihui.modules.enums;

/**
 * 文件类型
 * Created by Cutie on 2019/1/7.
 */
public enum FileTypeEnum {
    /** 图片 */
    PICTURE("图片","PICTURE"),
    /** PDF*/
    PDF("PDF","PDF"),
    /** WORD*/
    WORD("WORD","WORD"),
    /** EXCEL*/
    EXCEL("EXCEL","EXCEL"),
    /** PPT*/
    PPT("PPT","PPT"),
    /** 公示*/
    PUBLICITY("PUBLICITY","PUBLICITY"),
    /** 附件*/
    ACCESSORY("附件","ACCESSORY"),
    /** 对账单*/
    STATEMENTOFACCOUNT("对账单","STATEMENTOFACCOUNT")
    
    ;

    private String name;
    private String code;

    private FileTypeEnum(String name, String code) {
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
    public static FileTypeEnum getEnumByCode(String code){
        for(FileTypeEnum bizTypeEnum : FileTypeEnum.values()){
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
