package com.yeweihui.modules.common.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExcelService {

    void write(HttpServletResponse response, String sheetName, String fileName, List<List<String>> lists) throws Exception;

}
