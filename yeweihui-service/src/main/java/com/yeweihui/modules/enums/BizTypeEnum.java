package com.yeweihui.modules.enums;

/**
 * 业务类型
 * Created by Cutie on 2019/1/7.
 * 对应业务类型 1报销 2会议 3发函 4用章 5任务 6投票
 */
public enum BizTypeEnum {

    /**
     * 切勿改变以下code值!!!
     */
    BILL("报销",1),
    MEETING("会议",2),
    PAPER("发函",3),
    REQUEST("用章",4),
    TASK("任务",5),
    VOTE("投票",6),
    MEETING_LOG("会议日志",7),
    NOTICE("通知", 8),
    Announcement("公示", 9),
    /**
     * 记账
     */
    ACCOUNTS("记账", 10)
    ;

    private String name;
    private int code;

    private BizTypeEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }
    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (BizTypeEnum ex: BizTypeEnum.values()) {
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
        for (BizTypeEnum ex: BizTypeEnum.values()) {
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
    public static BizTypeEnum getEnumByCode(Integer code){
        for(BizTypeEnum bizTypeEnum : BizTypeEnum.values()){
            if(bizTypeEnum.getCode() == code){
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
