package com.rpc.core.serializer.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpc.common.enums.SerializerCodeEnum;
import com.rpc.common.exception.SerializeException;
import com.rpc.core.message.RpcRequest;
import com.rpc.core.message.RpcResponse;
import com.rpc.core.serializer.CommonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * kryo序列化
 * @author 21380
 */
@Slf4j
public class KryoSerializer implements CommonSerializer {

    /**
     * Kryo序列化会有线程安全问题，每个线程都需要有自己的Kryo对象，
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 将请求和响应两个类注册到kryo中，会自动生成序列化id
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        // 每个对象第一次出现在对象图中，会在记录时写入一个 varint，用于标记
        // 当此后有同一对象出现时，只会记录一个 varint，以此达到节省空间的目标
        kryo.setReferences(true);
        // 不强制要求注册类
        kryo.setRegistrationRequired(false);
        //Fix the NPE bug when deserializing Collections.
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
            .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) throws SerializeException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)){
            // 从ThreadLocal中获取到Kryo对象
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // 将对象写入output中
            kryo.writeObject(output, obj);
            // 使用完成后移除
            KRYO_THREAD_LOCAL.remove();
            // 转换为字节
            return output.toBytes();
        } catch (Exception e) {
            log.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) throws SerializeException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // 反序列化使用readObject读取成对象
            Object o = kryo.readObject(input, clazz);
            KRYO_THREAD_LOCAL.remove();
            return o;
        } catch (Exception e) {
            log.error("反序列化时有错误发生:", e);
            throw new SerializeException("反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerCodeEnum.KRY0.getCode();
    }
}
