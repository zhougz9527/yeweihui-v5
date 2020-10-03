package com.yeweihui.modules.vo.query;

import lombok.Data;

import java.util.List;

@Data
public class RequestMemberQueryParam {
    private Long requestId;
    private Integer requestMemberType;
    private Integer requestMemberVerifyStatus;
    private List<Integer> requestMemberVerifyStatusList;
}
