package syl.study.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * WebSocket 服务端
 *
 * @author 史彦磊
 * @create 2018-04-04 10:47.
 */
public class WebSocketServer {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //将请求和应答消息编码或几码为HTTP消息
                            pipeline.addLast("http-codec",new HttpServerCodec());
                            //将HTTP消息的多个部分组成一条完整的HTTP消息
                            pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                            //ChunkedWriteHandler来向客户端发送HTML5文件，主要用于支持浏览器和服务端进行WebSocket通信
                            pipeline.addLast("http-chunked",new ChunkedWriteHandler());

                            pipeline.addLast("handler",new WebSocketServerHandler());
                        }
                    });
            Channel channel = b.bind(port).sync().channel();
            System.out.println("Web socket server started at port "+port+".");
            System.out.println("Open your browser and navigate to http://localhost:"+port+"/");
            channel.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

}
