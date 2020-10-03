package com.yeweihui.modules.enums.vote;

import com.yeweihui.common.exception.RRException;

/**
 * 投票状态 1同意过半 2反对过半 3撤销 4选择确定 5人工决定
 * 进行中，通过，不通过，撤销
 * Created by Cutie on 2016/12/25.
 */
public enum VoteStatusEnum {
    进行中("进行中", 0),
    通过("通过", 1),
    不通过("不通过", 2),
    撤销("撤销", 3),
    选择确定("选择确定", 4),
    人工判断("人工判断", 5)
    ;

    private String name;
    private int code;

    private VoteStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (VoteStatusEnum ex: VoteStatusEnum.values()) {
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
        for (VoteStatusEnum ex: VoteStatusEnum.values()) {
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
