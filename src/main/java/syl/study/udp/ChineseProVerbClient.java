package syl.study.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


/**
 * @author 史彦磊
 * @create 2018-04-04 16:46.
 */
public class ChineseProVerbClient {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ChineseProverbClientHandler());
            Channel ch = b.bind(0).sync().channel();
            ch.writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer("姓名查询?", CharsetUtil.UTF_8),
                            new InetSocketAddress("255.255.255.255",port))).sync();
            if (!ch.closeFuture().await(15000)){
                System.out.println("查询超时！");
            }
        }finally {
            group.shutdownGracefully();
        }
    }
}
