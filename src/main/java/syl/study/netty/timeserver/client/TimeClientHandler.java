package syl.study.netty.timeserver.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 史彦磊
 * @create 2018-03-16 12:41.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    int counter;

    byte[] bytes;


    public TimeClientHandler() {
        bytes = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf firstMessage=null;
        for (int i = 0; i < 100; i++) {
            firstMessage = Unpooled.buffer(bytes.length);
            firstMessage.writeBytes(bytes);
            ctx.writeAndFlush(firstMessage);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String)msg;
        System.out.println("当前时间为:"+body+" ; counter: "+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
