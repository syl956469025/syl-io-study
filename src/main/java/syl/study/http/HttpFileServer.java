package syl.study.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author 史彦磊
 * @create 2018-03-19 17:44.
 */
public class HttpFileServer {

    private static final String default_url = "/src/main/java/syl/study/";

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //HTTP请求的解码器
                            ch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //httpObjectAggregator解码器，将多个消息转化为单一的FullHttpRequest或FullHttpResponse
                            ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            //响应消息的编码器
                            ch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            //支持异步发送打的码流，但不占用过多的内存，防止发生java内存溢出
                            ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            ch.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(default_url));
                        }
                    });
            ChannelFuture f = b.bind("127.0.0.1",port).sync();
            f.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }



}
