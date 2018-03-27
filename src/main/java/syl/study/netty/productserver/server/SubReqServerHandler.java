package syl.study.netty.productserver.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import syl.study.netty.productserver.SubscribeReq;
import syl.study.netty.productserver.SubscribeResp;
import syl.study.utils.FastJsonUtil;

/**
 * @author 史彦磊
 * @create 2018-03-19 10:49.
 */
public class SubReqServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq) msg;
        if ("syl".equals(req.getUserName())){
            System.out.println("服务端接收到的客户端请求为:"+ FastJsonUtil.bean2Json(req));
            ctx.writeAndFlush(resp(req.getSubReqID()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private SubscribeResp resp(int subReqID) {
        SubscribeResp resp = new SubscribeResp();
        resp.setSubReqID(subReqID);
        resp.setRespCode(0);
        resp.setDesc("netty 订单成功，3天后收货");
        return resp;

    }
}
