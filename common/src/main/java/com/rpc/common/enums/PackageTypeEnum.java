package com.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 21380
 */
@AllArgsConstructor
@Getter
public enum PackageTypeEnum {

    /**
     * 请求包
     */
    REQUEST_PACK(1),
    /**
     * 响应包
     */
    RESPONSE_PACK(2);
    private final int code;

}
