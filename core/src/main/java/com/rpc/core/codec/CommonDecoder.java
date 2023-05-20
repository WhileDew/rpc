package com.rpc.core.codec;

import com.rpc.common.constants.RpcError;
import com.rpc.common.enums.PackageTypeEnum;
import com.rpc.common.exception.RpcException;
import com.rpc.common.exception.SerializeException;
import com.rpc.core.message.RpcRequest;
import com.rpc.core.message.RpcResponse;
import com.rpc.core.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义解码器
 * @author 21380
 */
@Slf4j
public class CommonDecoder extends ByteToMessageDecoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws RpcException, SerializeException {
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER) {
            log.error("不识别的协议包: {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageCode = in.readInt();
        Class<?> packageClass;
        if(packageCode == PackageTypeEnum.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if(packageCode == PackageTypeEnum.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj;
        obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }
}
