package syl.study.ftp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author 史彦磊
 * @create 2018-04-04 18:14.
 */
public class FileServerHandler extends SimpleChannelInboundHandler<String> {

    private static final String CR = System.getProperty("");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        File file = new File(msg);
        if (file.exists()){
            if (!file.isFile()){
                ctx.writeAndFlush("Not a file : "+file+CR);
                return;
            }
            ctx.write(file + " "+ file.length()+CR);
            RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
            DefaultFileRegion region = new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
            ctx.write(region);
            ctx.writeAndFlush(CR);
            randomAccessFile.close();
        }else{
            ctx.writeAndFlush("file Not Found : "+file+CR);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
