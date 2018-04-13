package syl.study.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author 史彦磊
 * @create 2018-04-04 14:18.
 */
public class ChineseProverServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new ChineseProverserverHander());
            b.bind(port).sync().channel().closeFuture().await();
        }finally {
            group.shutdownGracefully();
        }
    }


}
