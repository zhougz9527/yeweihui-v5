package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.yeweihui.common.annotation.BeanFieldIndex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_room_temp")
public class BflyRoomTemp extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 操作人员id
    @TableField("operation_uid")
    private Long operationUid;

    // 小区名称
    @TableField("zone_id")
    private Long zoneId;

    // 苑
    @BeanFieldIndex(index = 1)
    private String court;

    // 幢
    @BeanFieldIndex(index = 2)
    private String building;

    // 单元号
    @BeanFieldIndex(index = 3)
    @TableField("unit_name")
    private String unitName;

    // 楼号
    @BeanFieldIndex(index = 4)
    @TableField("floor_name")
    private String floorName;

    // 建筑面积
    @BeanFieldIndex(index = 5)
    @TableField("housing_area")
    private BigDecimal housingArea;

    // 业主名字
    @BeanFieldIndex(index = 6)
    @TableField("user_name")
    private String userName;

    // 电话号码
    @BeanFieldIndex(index = 7)
    @TableField("phone_num")
    private String phoneNum;

    // 身份证号码
    @BeanFieldIndex(index = 8)
    @TableField("id_card")
    private String idCard;

    public BflyRoomTemp() {
    }

    public BflyRoomTemp(Long operationUid, Long zoneId) {
        this.operationUid = operationUid;
        this.zoneId = zoneId;
    }

    public void setOperationUid(Long operationUid) {
        this.operationUid = operationUid;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public void setHousingArea(BigDecimal housingArea) {
        this.housingArea = housingArea;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
