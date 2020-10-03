package com.yeweihui.modules.division.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("division_community")
public class CommunityEntity {

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
     * 街道Id
     */
    private Long subdistrictId;

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

    public Long getSubdistrictId() {
        return subdistrictId;
    }

    public void setSubdistrictId(Long subdistrictId) {
        this.subdistrictId = subdistrictId;
    }
}
