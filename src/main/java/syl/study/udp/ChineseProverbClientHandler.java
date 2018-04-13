package syl.study.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @author 史彦磊
 * @create 2018-04-04 16:48.
 */
public class ChineseProverbClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        String s = msg.content().toString(CharsetUtil.UTF_8);
        if (s.startsWith("姓名查询结果：")){
            System.out.println(s);
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
