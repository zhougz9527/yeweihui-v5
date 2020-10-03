package com.yeweihui.modules.vo.admin.file;

import lombok.Data;

import java.util.Date;

@Data
public class FileEntity {
    private String name;
    private String percentage;
    private FileRawEntity raw;
    private ResponseEntity response;
    private String size;
    private String status;
    private String uid;
    private String url;
    private Date createTime;
}
