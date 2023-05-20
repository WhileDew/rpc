package com.rpc.core.serializer;

import com.rpc.common.exception.SerializeException;
import com.rpc.core.serializer.impl.HessianSerializer;
import com.rpc.core.serializer.impl.JsonSerializer;
import com.rpc.core.serializer.impl.KryoSerializer;

/**
 * @author 21380
 */
public interface CommonSerializer {

    /**
     * 序列化接口
     * @param obj 序列化对象
     * @return 字节数组
     * @throws SerializeException 异常
     */
    byte[] serialize(Object obj) throws SerializeException;

    /**
     * 序列化接口
     * @param bytes 字节数组
     * @param clazz 反序列化类型
     * @return 反序列化后的对象
     * @throws SerializeException 异常
     */
    Object deserialize(byte[] bytes, Class<?> clazz) throws SerializeException;

    /**
     * 获取序列化码
     * @return 序列化码
     */
    int getCode();

    /**
     * 根据序列化码获取序列化器
     * @param code 序列化码
     * @return 序列化器
     */
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 1:
                return new HessianSerializer();
            case 2:
                return new JsonSerializer();
            default:
                return new KryoSerializer();
        }
    }
}
