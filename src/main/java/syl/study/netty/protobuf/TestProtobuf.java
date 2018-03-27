package syl.study.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 史彦磊
 * @create 2018-03-19 15:49.
 */
public class TestProtobuf {


    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }


    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("syl");
        builder.setProductName("Netty Book");
        List<String> address = new ArrayList<>();
        address.add("北京市顺义区");
        address.add("北京市朝阳区");
        address.add("北京市海淀区");
        builder.addAllAddress(address);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println(req.toString());
        SubscribeReqProto.SubscribeReq decode = decode(encode(req));
        System.out.println("After decode:"+req.toString());
        System.out.println("Assert equal : -->" +decode.equals(req));
    }



}
