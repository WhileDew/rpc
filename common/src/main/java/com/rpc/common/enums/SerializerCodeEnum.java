package com.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化枚举类
 * @author 21380
 */
@AllArgsConstructor
@Getter
public enum SerializerCodeEnum {

    /**
     * KRYO序列化
     */
    KRY0("KRYO", 0),
    /**
     * HESSIAN序列化
     */
    HESSIAN("HESSIAN", 1),
    /**
     * JSON序列化
     */
    JSON("JSON", 2);

    private final String name;
    private final int code;
}
