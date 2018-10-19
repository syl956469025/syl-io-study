package syl.custom.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import syl.custom.codec.NettyMessageDecoder;
import syl.custom.codec.NettyMessageEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 私有协议客户端代码
 *
 * @author 史彦磊 yanlei.shi
 * @create 2018/4/17
 */
public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    private static final int port = 18080;
    private static final String host = "127.0.0.1";


    public void connect() throws InterruptedException {
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new NettyMessageDecoder(1024*1024,4,4));
                            p.addLast("MessageEncoder",new NettyMessageEncoder());
                            p.addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            p.addLast("LoginAuthHandler",new LoginAuthReqHandler());
                            p.addLast("HeartBeatHandler",new HeartBeatReqHandler());
                        }
                    });
            ChannelFuture f = b.connect(new InetSocketAddress(host, port), new InetSocketAddress(host, 8081)).sync();
            f.channel().closeFuture().sync();

        }finally {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try{
                        TimeUnit.SECONDS.sleep(50);
                        connect();//发起重连操作
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect();
    }


}
