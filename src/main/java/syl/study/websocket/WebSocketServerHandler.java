package syl.study.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author 史彦磊
 * @create 2018-04-04 10:51.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = Logger.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传统的HTTP接入
        if (msg instanceof FullHttpRequest){
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }else if (msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx,(WebSocketFrame)msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是关闭链路的指令
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)frame.retain());
            return;
        }
        //判断是不是ping消息
        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //本例程只支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
        }
        //返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
        if (log.isLoggable(Level.FINE)){
            log.fine(String.format("%s received %s",ctx.channel(),request));
        }
        ctx.channel().write(new TextWebSocketFrame(request+", 欢迎使用Netty WebSocket服务，现在时刻："+ LocalDateTime.now().toString()));
    }

    /**
     * 第一次握手请求消息由HTTP协议承载
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //如果HTTP解码失败，返回HTTP异常
        if (!req.decoderResult().isSuccess()|| (!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        //构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),req);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse response) {
        //返回给客户端
        if (response.status().code() != 200){
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(response,response.content().readableBytes());
        }
        //如果是非Keep-Alive 关闭连接
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        if (!HttpUtil.isKeepAlive(req) || response.status().code() != 200){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
