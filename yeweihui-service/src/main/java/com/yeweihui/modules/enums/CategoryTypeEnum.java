package com.yeweihui.modules.enums;

/**
 * 分类类型
 * Created by Cutie on 2016/7/29.
 */
public enum CategoryTypeEnum {
    SONG("歌曲","SONG"),
    //分类大类
    SONG_MAIN("歌曲分类大类","SONG_MAIN"),
    SINGER_MAIN("歌手分类大类","SINGER_MAIN"),
    ARTICLE_MAIN("文章分类大类","ARTICLE_MAIN"),
    VIDEO_MAIN("视频分类大类","VIDEO_MAIN"),
    TINY_VIDEO_MAIN("小视频分类大类","TINY_VIDEO_MAIN"),
    PLAYLIST_MAIN("歌单分类大类","PLAYLIST_MAIN"),

    INTERACTION_COMMENT_MAIN("虚假评论分类大类","INTERACTION_COMMENT_MAIN"),
    INTERACTION_COMMENT("虚假评论分类小类","INTERACTION_COMMENT"),
    INTERACTION_COMMON_COMMENT_MAIN("虚假评论普通分类大类","INTERACTION_COMMON_COMMENT_MAIN"),
    ;

    private String name;
    private String code;

    private CategoryTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (CategoryTypeEnum ex: CategoryTypeEnum.values()) {
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
        for (CategoryTypeEnum ex: CategoryTypeEnum.values()) {
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
