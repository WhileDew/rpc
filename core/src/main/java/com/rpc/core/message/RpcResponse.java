package com.rpc.core.message;


import com.rpc.common.enums.ResponseCodeEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 21380
 */
@Data
@ToString
public class RpcResponse extends AbstractMessage implements Serializable {
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private Object data;

    public static RpcResponse success(Object data) {
        RpcResponse response = new RpcResponse();
        response.setStatusCode(ResponseCodeEnum.SUCCESS.getCode());
        response.setData(data);
        return response;
    }
    public static RpcResponse fail(ResponseCodeEnum code) {
        RpcResponse response = new RpcResponse();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
