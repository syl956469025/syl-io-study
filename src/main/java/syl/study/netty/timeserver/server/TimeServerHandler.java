package syl.study.netty.timeserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.time.LocalDateTime;

/**
 * @author 史彦磊
 * @create 2018-03-16 11:36.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;



    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String body = (String)msg;
        System.out.println("时间服务器接收到的订单为:"+body +" ; counter: "+ ++counter);
        String currentTime = LocalDateTime.now().toString()+System.getProperty("line.separator");
        ByteBuf byteBuf = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(byteBuf);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
