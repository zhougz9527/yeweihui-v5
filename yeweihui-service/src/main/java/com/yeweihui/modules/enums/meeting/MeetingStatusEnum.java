package com.yeweihui.modules.enums.meeting;

import com.yeweihui.common.exception.RRException;

/**
 * 状态 0待签到 1进行中 2待签字 3结束 4取消
 * Created by Cutie on 2016/12/25.
 */
public enum MeetingStatusEnum {
    未开始("未开始", 0),
    进行中("进行中", 1),
    待签字("待签字", 2),
    结束("结束", 3),
    取消("取消", 4),
    ;

    private String name;
    private int code;

    private MeetingStatusEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (MeetingStatusEnum ex: MeetingStatusEnum.values()) {
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
        for (MeetingStatusEnum ex: MeetingStatusEnum.values()) {
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
