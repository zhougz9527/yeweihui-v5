package com.yeweihui.modules.bfly.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.Date;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bfly_room_vote_snapshot")
public class BflyRoomVoteSnapshot extends BaseEntity{

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("vote_id")
    private Long voteId;
    @TableField("user_vote_id")
    private Long userVoteId;
    @TableField("zone_id")
    private Long zoneId;
//    @TableField("zone_name")
//    private String zone_name;
    @TableField("court")
    private String court;
    @TableField("building")
    private String building;
    @TableField("unit_name")
    private String unitName;
    @TableField("floor_name")
    private String floorName;
    @TableField("housing_area")
    private BigDecimal housingArea;
    @TableField("user_name")
    private String userName;
    @TableField("phone_num")
    private String phoneNum;
    @TableField("id_card")
    private String idCard;


    public BflyRoomVoteSnapshot() {

    }

    public BflyRoomVoteSnapshot(BflyRoom bflyRoom) {
        this.court = bflyRoom.getCourt();
        this.building = bflyRoom.getBuilding();
        this.unitName = bflyRoom.getUnitName();
        this.floorName = bflyRoom.getFloorName();
        this.housingArea = bflyRoom.getHousingArea();
        this.userName = bflyRoom.getUserName();
        this.phoneNum = bflyRoom.getPhoneNum();
        this.idCard = bflyRoom.getIdCard();
    }
}
