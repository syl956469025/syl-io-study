package syl.study.ftp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author 史彦磊
 * @create 2018-04-04 18:05.
 */
public class FileServer {


    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        EventLoopGroup bossgroup = new NioEventLoopGroup();
        EventLoopGroup workgroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossgroup,workgroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //LineBasedFrameDecoder和StringDecoder组合就是文本换行解码器
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new FileServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            System.out.println("Start file server at port : "+port);
            f.channel().closeFuture().sync();
        }finally {
            bossgroup.shutdownGracefully();
            workgroup.shutdownGracefully();
        }


    }

}
