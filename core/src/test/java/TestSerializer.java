import com.rpc.common.exception.RpcException;
import com.rpc.common.exception.SerializeException;
import com.rpc.core.message.RpcRequest;
import com.rpc.core.serializer.impl.HessianSerializer;

import java.util.Arrays;

public class TestSerializer {

    public static void main(String[] args) throws RpcException, SerializeException {
        HessianSerializer hessianSerializer = new HessianSerializer();
        RpcRequest rpcRequest = new RpcRequest("1", "2", new Object[]{Integer.valueOf(3)}, new Class[]{Integer.class});
        byte[] serialize = hessianSerializer.serialize(rpcRequest);
        System.out.println(Arrays.toString(serialize));
        Object deserialize = hessianSerializer.deserialize(serialize, RpcRequest.class);
        System.out.println(deserialize);
    }
}
