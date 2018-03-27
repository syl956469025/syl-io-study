package syl.study.http.xml.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import syl.study.http.xml.Order;

import java.net.InetSocketAddress;

/**
 * @author 史彦磊
 * @create 2018-03-23 17:10.
 */
public class HttpXmlClient {

    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // xml解码器
                            ch.pipeline().addLast("http-decoder", new HttpResponseDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("xml-decoder", new HttpXmlResponseDecoder(Order.class, true));
                            ch.pipeline().addLast("http-encoder", new HttpRequestEncoder());
                            // xml编码器
                            ch.pipeline().addLast("xml-encoder", new HttpXmlRequestEncoder());
                            ch.pipeline().addLast("xmlClientHandler", new HttpXmlClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(new InetSocketAddress(port)).sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
