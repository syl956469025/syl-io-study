package syl.study.netty.echoserver.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 史彦磊
 * @create 2018-03-16 17:00.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    static final String ECHO_REQ = "hello world";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] bytes = ECHO_REQ.getBytes();
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(bytes));
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("这是第"+ ++counter + " 次接收服务端的消息： "+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
