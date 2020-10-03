package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_user")
public class BflyUser extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 小区id
    @TableField("zone_id")
    private Long zoneId;

    // 房屋id
    @TableField("bfly_room_id")
    private Long bflyRoomId;

    // 微信昵称
    private String nickname;

    // 业主姓名
    private String username;

    // 电话号码
    @TableField("phone_num")
    private String phoneNum;

    // 身份证号码
    @TableField("id_card")
    private String idCard;

    // 房屋地址
    private String address;

    // 房屋建筑面积
    @TableField("housing_area")
    private BigDecimal housingArea;

    // 身份证url1
    @TableField("id_card_url")
    private String idCardUrl;

    // 身份证url2
    @TableField("id_card_url_extra1")
    private String idCardUrlExtra1;

    // 房产证图片url1
    @TableField("house_certificate_url")
    private String houseCertificateUrl;

    // 房产证图片url2
    @TableField("house_certificate_url_extra1")
    private String houseCertificateUrlExtra1;

    // 用户上传的头像
    @TableField("header_url")
    private String headerUrl;

    // 公众号open id
    @TableField("public_open_id")
    private String publicOpenId;

    // 小程序open id
    @TableField("open_id")
    private String openId;

    // 多账户唯一id
    @TableField("union_id")
    private String unionId;

    // 微信头像
    private String avatar;

    // 微信返回的用户json数据
    @TableField("wechat_user_json")
    private String wechatUserJson;

    // 0:未审核, 1:已通过 2:未通过
    @TableField("status")
    private Integer status;

    // 最近一次认证时间
    @TableField("last_cert_time")
    private Timestamp lastCertTime;

    // 最近一次登陆时间
    @TableField("last_login")
    private Timestamp lastLogin;

    // 0:无效 1:有效
    @TableField("is_valid")
    private Integer isValid;

    // 苑
    @TableField(exist = false)
    private String court;

    // 幢
    @TableField(exist = false)
    private String building;

    // 楼号
    @TableField(exist = false)
    private String floorName;

    // 单元号
    @TableField(exist = false)
    private String unitName;

    // 小区名称
    @TableField(exist = false)
    private String zoneName;

    // 登录token
    @TableField(exist = false)
    private String token;

}
