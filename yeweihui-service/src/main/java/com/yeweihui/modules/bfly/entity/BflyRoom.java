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
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_room")
public class BflyRoom extends BaseEntity {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 操作人员id
    @TableField("operation_uid")
    private Long operationUid;

    @TableField(exist = false)
    private String zoneName;

    // 小区id
    @TableField("zone_id")
    private Long zoneId;

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

    // 建筑面积
    @TableField("housing_area")
    private BigDecimal housingArea;

    // 业主名字
    @TableField("user_name")
    private String userName;

    // 电话号码
    @TableField("phone_num")
    private String phoneNum;

    // 身份证号码
    @TableField("id_card")
    private String idCard;

    @TableField("last_certification_time")
    private Timestamp lastCertificationTime;

    @TableField(exist = false)
    private Integer userStatus;

    public BflyRoom() {
    }

    public BflyRoom(Long id, Long operationUid, Long zoneId, String court, String building, String floorName, String unitName, BigDecimal housingArea, String userName, String phoneNum, String idCard) {
        this.id = id;
        this.operationUid = operationUid;
        this.zoneId = zoneId;
        this.court = court;
        this.building = building;
        this.floorName = floorName;
        this.unitName = unitName;
        this.housingArea = housingArea;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.idCard = idCard;
    }

    public BflyRoom(BflyRoomTemp roomTemp) {
        this.operationUid = roomTemp.getOperationUid();
        this.zoneId = roomTemp.getZoneId();
        this.court = roomTemp.getCourt();
        this.building = roomTemp.getBuilding();
        this.floorName = roomTemp.getFloorName();
        this.unitName = roomTemp.getUnitName();
        this.housingArea = roomTemp.getHousingArea();
        this.userName = roomTemp.getUserName();
        this.phoneNum = roomTemp.getPhoneNum();
        this.idCard = roomTemp.getIdCard();
    }

}
