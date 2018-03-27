package syl.study.netty.protobuf.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import syl.study.netty.protobuf.SubscribeReqProto;
import syl.study.netty.protobuf.SubscribeResqProto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 史彦磊
 * @create 2018-03-19 16:36.
 */
public class SubReqClientProtobufHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeResqProto.SubscribeResq resp = (SubscribeResqProto.SubscribeResq) msg;
        System.out.println("接收到服务端的回复:"+resp.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subreq(i));
        }
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subreq(int subId){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(subId);
        builder.setProductName("Netty book");
        builder.setUserName("syl");
        List<String> address = new ArrayList<>();
        address.add("北京市朝阳区");
        address.add("北京市海淀区");
        builder.addAllAddress(address);
        return builder.build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
