package com.yeweihui.modules.division.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("division_district")
public class DistrictEntity {

    /**
     * Id
     */
    @TableId
    private Long id;

    /**
     * 名
     */
    private String name;

    /**
     * 市Id
     */
    private Long cityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
