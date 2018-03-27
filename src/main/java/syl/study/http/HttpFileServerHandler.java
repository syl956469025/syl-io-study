package syl.study.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;

/**
 * @author 史彦磊
 * @create 2018-03-19 17:51.
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if (request.method() != HttpMethod.GET){
            sendError(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        final String uri = request.uri();
        final String path = sanitizeUri(uri);
        System.out.println("path:"+path);
        if (path == null){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() || !file.exists()){
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }
        if (file.isDirectory()){
            if (uri.endsWith("/")){
                sendListing(ctx,file);
            }else{
                sendRedirect(ctx,uri+"/");
            }
            return;
        }
        if (!file.isFile()){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file,"r");
        }catch (FileNotFoundException e){
            sendError(ctx,HttpResponseStatus.NOT_FOUND);
            return;
        }
        long fileLength = randomAccessFile.length();
        System.out.println("fileLength:"+fileLength);
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(response,fileLength);
        setContentTypeHeader(response,file);
        if (HttpUtil.isKeepAlive(request)){
            response.headers().set(CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        }
        response.headers().set("Content-Type","json;charset=UTF-8");
        ctx.write(response);
        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile,0,fileLength,8192),ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total <0){
                    System.err.println("Transfer progress: "+progress);
                }else{
                    System.err.println("Transfer progress: "+progress+"/"+total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("Transfer complete");
            }
        });
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpUtil.isKeepAlive(request)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()){
            sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("meiwenti");
    }

    private static void setContentTypeHeader(HttpResponse response, File file){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE,mimetypesFileTypeMap.getContentType(file.getPath()));
    }

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error(e1);
            }
        }
        System.out.println("uri 为:"+uri);
        if (!uri.startsWith(url)){
            return null;
        }
        if (!uri.startsWith("/")){
            return null;
        }
        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator+'.')
                || uri.contains('.'+File.separator)
                ||uri.startsWith(".")
                ||uri.endsWith(".")){
            return null;
        }
        return System.getProperty("user.dir")+File.separator+uri;

    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse
                (HttpVersion.HTTP_1_1,status, Unpooled.copiedBuffer("Failure: "+status+"\r\n", CharsetUtil.UTF_8));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    private static void sendRedirect(ChannelHandlerContext ctx,String newUrl){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.FOUND);
        response.headers().set(LOCATION,newUrl);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendListing(ChannelHandlerContext ctx,File dir){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set("Content-Type","text/html;charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        String path = dir.getPath();
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(path);
        buf.append(" 目录：");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append(path).append(" 目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>连接：<a href=\"../\">..</a></li>\r\n");
        for (File file : dir.listFiles()) {
            if (file.isHidden()|| !file.canRead()){
                continue;
            }
            String name = file.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()){
                continue;
            }
            buf.append("<li>连接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);


    }
}
