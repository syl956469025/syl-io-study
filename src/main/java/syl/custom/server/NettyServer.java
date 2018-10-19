package syl.custom.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import syl.custom.codec.NettyMessageDecoder;
import syl.custom.codec.NettyMessageEncoder;


/**
 * 服务端
 *
 * @author 史彦磊 yanlei.shi
 * @create 2018/4/17
 */
public class NettyServer {

    private static final int port = 18080;
    private static final String host = "127.0.0.1";


    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new NettyMessageDecoder(1024*1024,4,4));
                        p.addLast(new NettyMessageEncoder());
                        p.addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                        p.addLast(new LoginAuthRespHandler());
                        p.addLast("HeartBeatHandler",new HeartBeatRespHandler());
                    }
                });
        ChannelFuture f = b.bind(host, port).sync();
        System.out.println("netty服务启动成功 host:"+host+"   端口为："+port);
    }


}
