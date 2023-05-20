package com.rpc.core.serializer.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.rpc.common.enums.SerializerCodeEnum;
import com.rpc.common.exception.SerializeException;
import com.rpc.core.serializer.CommonSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian序列化
 * @author 21380
 */
public class HessianSerializer implements CommonSerializer {

    /**
     * 序列化
     * @param obj 序列化对象
     * @return 字节数组
     */
    public byte[] serialize(Object obj) throws SerializeException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Hessian2Output ho = new Hessian2Output(out);
            ho.writeObject(obj);
            ho.flush();
            return out.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SerializeException("11111");
        }
    }

    /**
     * 反序列化
     *
     * @param bytes 字节
     * @param clazz clazz
     * @return {@code T}
     */
    public Object deserialize(byte[] bytes, Class<?> clazz) throws SerializeException {
        Hessian2Input hi = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            hi = new Hessian2Input(bais);
            return hi.readObject();
        } catch (Exception ex) {
            throw new SerializeException("deserialize failed");
        } finally {
            if (null != hi) {
                try {
                    hi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bais) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int getCode() {
        return SerializerCodeEnum.HESSIAN.getCode();
    }
}
