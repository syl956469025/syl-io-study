package syl.study.netty.productserver.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import syl.study.netty.productserver.SubscribeReq;
import syl.study.netty.productserver.SubscribeResp;
import syl.study.utils.FastJsonUtil;

/**
 * @author 史彦磊
 * @create 2018-03-19 14:18.
 */
public class SubReqClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeResp resp = (SubscribeResp) msg;
        System.out.println("接收到服务端的结果为:"+ FastJsonUtil.bean2Json(resp));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private SubscribeReq subReq(int id){
        SubscribeReq req = new SubscribeReq();
        req.setAddress("北京市朝阳区温特莱中心B座");
        req.setPhoneNumber("12345678912");
        req.setProductName("Netty 权威指南");
        req.setSubReqID(id);
        req.setUserName("syl");
        return req;
    }
}
