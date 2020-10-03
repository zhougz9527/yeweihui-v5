package com.yeweihui.modules.enums.vote;

import com.yeweihui.common.exception.RRException;

/**
 * 状态 1同意 2反对 3弃权 4超时
 * Created by Cutie on 2016/12/25.
 */
public enum VoteMemberStatusEnum {
    待操作("待操作", 0),
    同意("同意", 1),
    反对("反对", 2),
    弃权("弃权", 3),
    超时("超时", 4),
    多选已表决("多选已表决", 5),
    ;

    private String name;
    private int code;

    private VoteMemberStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (VoteMemberStatusEnum ex: VoteMemberStatusEnum.values()) {
            if (ex.getCode()==code) {
                return ex.getName();
            }
        }
        throw new RRException("找不到对应名称的枚举name");
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static int getCode(String name) {
        for (VoteMemberStatusEnum ex: VoteMemberStatusEnum.values()) {
            if (ex.getName().equals(name)) {
                return ex.getCode();
            }
        }
        throw new RRException("找不到对应名称的枚举code");
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
