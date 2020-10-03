package com.yeweihui.modules.enums;

/**
 * 消息关联类型
 * Created by Cutie on 2016/7/29.
 */
public enum MessageTypeEnum {
    FOLLOW("粉丝关注","FOLLOW"),
    THUMB_UP("点赞","THUMB_UP"),
    COMMENT("评论","COMMENT"),
    PUSH("推送","PUSH"),
    ;

    private String name;
    private String code;

    private MessageTypeEnum(String name, String code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(String code) {
        for (MessageTypeEnum ex: MessageTypeEnum.values()) {
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
        for (MessageTypeEnum ex: MessageTypeEnum.values()) {
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
