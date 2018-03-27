package syl.study.netty.protobuf.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import syl.study.netty.protobuf.SubscribeReqProto;
import syl.study.netty.protobuf.SubscribeResqProto;

/**
 * @author 史彦磊
 * @create 2018-03-19 16:07.
 */
public class SubReqServerProbufHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("syl".equals(req.getUserName())){
            System.out.println("服务端接收到的订单是:"+req.toString());
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private SubscribeResqProto.SubscribeResq resp(int subReqID) {
        SubscribeResqProto.SubscribeResq.Builder builder = SubscribeResqProto.SubscribeResq.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(0);
        builder.setDesc("北京市朝阳区");
        return builder.build();

    }
}
