package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_user_cert_record")
public class BflyUserCertRecord extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //用户id
    @TableField("bfly_user_id")
    private Long bflyUserId;

    // 房屋id
    @TableField("bfly_room_id")
    private Long bflyRoomId;

    // 苑
    private String court;

    // 幢
    private String building;

    // 楼号
    @TableField("floor_name")
    private String floorName;

    // 单元号
    @TableField("unit_name")
    private String unitName;

    // 提交审核的业主姓名
    @TableField("check_user_name")
    private String checkUserName;

    // 提交审核的业主电话号码
    @TableField("check_phone_num")
    private String checkPhoneNum;

    // 提交审核的房屋建筑面积
    @TableField("check_housing_area")
    private BigDecimal checkHousingArea;

    // 提交审核的业主身份证号码
    @TableField("check_id_card")
    private String checkIdCard;

    // 提交审核的业主身份证图片
    @TableField("check_id_card_url")
    private String checkIdCardUrl;

    // 提交审核的业主身份证图片
    @TableField("check_id_card_url_extra1")
    private String checkIdCardUrlExtra1;

    // 提交审核的业主房产证图片
    @TableField("check_house_certificate_url")
    private String checkHouseCertificateUrl;

    // 提交审核的业主房产证图片
    @TableField("check_house_certificate_url_extra1")
    private String checkHouseCertificateUrlExtra1;

    // 提交审核的业主头像
    @TableField("check_header_url")
    private String checkHeaderUrl;

    // 审核状态 0: 待审核 1:审核通过 2:审核不通过
    @TableField("status")
    private Integer status;

    // 0:手动提交 1：自动提交
    @TableField("submitMethod")
    private Integer submitMethod;


    //小区名称
    @TableField(exist = false)
    private String zoneName;

}
