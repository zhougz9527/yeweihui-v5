package com.yeweihui.modules.vo.admin.file;

import lombok.Data;

@Data
public class FileRawEntity {
    private String lastModified;
    private String lastModifiedDate;
    private String name;
    private String size;
    private String type;
    private String uid;
    private String webkitRelativePath;
}
