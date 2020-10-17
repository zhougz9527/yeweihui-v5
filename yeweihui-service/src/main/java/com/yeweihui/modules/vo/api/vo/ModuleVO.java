package com.yeweihui.modules.vo.api.vo;


import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleVO implements Serializable {

    /** 父页面的名称 */
    private String page;

    /** key: 模块类型, value: 模块名List */
    private Map<String, List<String>> moduleMap;

    public ModuleVO(String pageName) {
        page = pageName;
        moduleMap = new HashMap<>();
    }


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Map<String, List<String>> getModuleMap() {
        return moduleMap;
    }

    public void setModuleMap(Map<String, List<String>> moduleMap) {
        this.moduleMap = moduleMap;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
