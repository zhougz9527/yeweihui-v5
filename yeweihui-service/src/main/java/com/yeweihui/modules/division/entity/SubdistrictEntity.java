package com.yeweihui.modules.division.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("division_subdistrict")
public class SubdistrictEntity {

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
     * 区Id
     */
    private Long districtId;

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

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
