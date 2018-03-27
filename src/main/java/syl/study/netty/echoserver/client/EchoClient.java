package syl.study.netty.echoserver.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author 史彦磊
 * @create 2018-03-16 16:55.
 */
public class EchoClient {


    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加特殊字符分隔解码器
//                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                            //添加固定长度解码器
                            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = b.connect("127.0.0.1", 8080).sync();
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
        }

    }

}
