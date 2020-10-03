package com.yeweihui.modules.enums;

/**
 * 图片
 * Created by Cutie on 2016/7/29.
 */
public enum PictureEnum {
    ARTICLE_COVER("文章封面","ARTICLE_COVER"),//1图或者3图

    IDENTITY_ID_FRONT("用户认证身份证正面","IDENTITY_ID_FRONT"),
    ;

    private String name;
    private String code;

    private PictureEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (PictureEnum ex: PictureEnum.values()) {
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
        for (PictureEnum ex: PictureEnum.values()) {
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
