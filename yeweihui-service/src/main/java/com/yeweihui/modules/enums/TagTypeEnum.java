package com.yeweihui.modules.enums;

/**
 * 标签类型
 * Created by Cutie on 2016/7/29.
 */
public enum TagTypeEnum {
    SINGER("歌手类型","SINGER"),
    SONG("歌曲类型","SONG"),
    VIDEO("视频类型","VIDEO"),
    TINY_VIDEO("视频类型","TINY_VIDEO"),
    ARTICLE("文章类型","ARTICLE"),
    ;

    private String name;
    private String code;

    private TagTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (TagTypeEnum ex: TagTypeEnum.values()) {
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
        for (TagTypeEnum ex: TagTypeEnum.values()) {
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
