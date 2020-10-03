package com.yeweihui.common.utils;

import com.yeweihui.modules.enums.FileTypeEnum;
import org.apache.commons.lang.StringUtils;

public class FileHelper {

    private FileHelper() { }

    public static FileTypeEnum getFileTypeEnumByName(String fileNameOrig) {
        if (StringUtils.isNotBlank(fileNameOrig)) {
            String fileName = fileNameOrig.toLowerCase();
            if (fileName.contains("doc") || fileName.contains("docx")) {
                return FileTypeEnum.WORD;
            } else if (fileName.contains("xls") || fileName.contains("xlsx")) {
                return FileTypeEnum.EXCEL;
            } else if (fileName.contains("ppt") || fileName.contains("pptx")) {
                return FileTypeEnum.PPT;
            } else if (fileName.contains("pdf")) {
                return FileTypeEnum.PDF;
            }
        }
        return FileTypeEnum.PICTURE;
    }
}
