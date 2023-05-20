package com.rpc.core.codec;

import com.rpc.common.enums.PackageTypeEnum;
import com.rpc.common.exception.SerializeException;
import com.rpc.core.message.AbstractMessage;
import com.rpc.core.message.RpcRequest;
import com.rpc.core.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
        +---------------+---------------+-----------------+-------------+
        |  Magic Number |  Package Type | Serializer Type | Data Length |
        |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
        +---------------+---------------+-----------------+-------------+
        |                          Data Bytes                           |
        |                   Length: ${Data Length}                      |
        +---------------------------------------------------------------+

 * 自定义编码器
 * @author 21380
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws SerializeException {
        out.writeInt(MAGIC_NUMBER);
        // 判断是哪个实例
        if(msg instanceof RpcRequest) {
            out.writeInt(PackageTypeEnum.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageTypeEnum.RESPONSE_PACK.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes;
        bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}