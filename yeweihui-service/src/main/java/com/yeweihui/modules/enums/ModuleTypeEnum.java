package com.yeweihui.modules.enums;

/**
 * 模块
 * 团推关联类型：groupPush
 * 评论关联类型：comment
 * 浏览历史关联类型：viewHistory
 * 消息关联类型：message
 * Created by Cutie on 2016/7/29.
 * VIDEO 视频 TINY_VIDEO 短视频 ARTICLE 文章 SONG 歌曲 SINGER 歌手
 */
public enum ModuleTypeEnum {
    VIDEO("视频","VIDEO"),
    TINY_VIDEO("短视频","TINY_VIDEO"),
    ARTICLE("文章","ARTICLE"),
    SONG("歌曲","SONG"),
    SINGER("歌手","SINGER"),

    SONG_TINY_VIDEO("使用该歌曲的小视频","SONG_TINY_VIDEO"),
    ;

    private String name;
    private String code;

    private ModuleTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (ModuleTypeEnum ex: ModuleTypeEnum.values()) {
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
        for (ModuleTypeEnum ex: ModuleTypeEnum.values()) {
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
