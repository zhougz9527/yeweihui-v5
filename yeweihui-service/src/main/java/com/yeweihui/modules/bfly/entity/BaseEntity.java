package com.yeweihui.modules.bfly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

@Data
public abstract class BaseEntity {

    @JsonIgnore
    private Timestamp createdAt;

    @JsonIgnore
    private Timestamp updatedAt;

}
