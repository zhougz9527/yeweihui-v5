package com.yeweihui.modules.enums.bfly;

import com.yeweihui.common.exception.RRException;

public enum MagicEnum {

    SHOP("商铺", "商铺"),
    HOUSE("排屋", "排屋"),
    // 用户表决表状态
    用户表决未参会("未参会", 0),
    用户表决已参会("已参会", 1),
    用户表决已投票("已投票", 2),
    用户表决无效("无效", 3),
    // 表决表状态
    表决未开始("未开始",  1),
    表决参会中("参会中", 2),
    表决参会结束("参会结束", 3),
    表决投票中("投票中", 4),
    表决投票结束("投票结束", 5),
    // 表决显示状态
    表决隐藏("隐藏", 0),
    表决显示("显示", 1),
    // 用户表房屋审核状态
    未审核("未审核", 0),
    审核通过("审核通过", 1),
    审核未通过("审核未通过", 2),
    审核中("审核中", 3),
    已撤销("已撤销", 4),
    // 用户房屋有效状态
    无效("无效", 0),
    有效("有效", 1),
    //bfly_user_cert_record表 房屋审核方式
    手动审核("手动审核",0),
    自动审核("自动审核",1),
    // bfly_user_cert_record表 房屋审核状态
    房屋记录待审核("房屋记录待审核", 0),
    房屋记录审核通过("房屋记录审核通过", 1),
    房屋记录审核未通过("房屋记录审核未通过", 2);


    private String name;
    private Object code;

    private MagicEnum(String name, Object code) {
        this.name = name;
        this.code = code;
    }

    /**
     * 根据code获取对应状态值
     */
    public static String getName(Object code) {
        for (MagicEnum ex: MagicEnum.values()) {
            if (ex.getCode().equals(code)) {
                return ex.getName();
            }
        }
        throw new RRException("找不到对应名称的枚举name");
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static Object getCode(String name) {
        for (MagicEnum ex: MagicEnum.values()) {
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

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }
}
