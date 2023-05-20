package com.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 21380
 */
@AllArgsConstructor
@Getter
public enum ResponseCodeEnum {

    /**
     * 响应成功
     */
    SUCCESS(200, "The remote call is successful"),
    /**
     * 响应失败
     */
    FAIL(500, "The remote call is fail");
    private final int code;

    private final String message;

}
