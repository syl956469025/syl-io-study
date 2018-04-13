package syl.study.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 史彦磊
 * @create 2018-04-04 16:28.
 */
public class ChineseProverserverHander extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[] DICTIONARY = {"张三","李四","王五","张三","李四","王五"};

    private String nextQuote(){
        int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[quoteId];
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String req = packet.content().toString(CharsetUtil.UTF_8);
        System.out.println(req);
        if ("姓名查询?".equals(req)){
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("姓名查询结果："+nextQuote(), CharsetUtil.UTF_8),packet.sender()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
