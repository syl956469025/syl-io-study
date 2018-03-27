package syl.study.http.xml.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import syl.study.http.xml.Address;
import syl.study.http.xml.Order;
import syl.study.http.xml.message.HttpXmlRequest;
import syl.study.http.xml.message.HttpXmlResponse;
import syl.study.utils.FastJsonUtil;

import java.util.ArrayList;
import java.util.List;

import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Values.KEEP_ALIVE;

/**
 * @author 史彦磊
 * @create 2018-03-23 17:22.
 */
public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {





    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpXmlRequest req) throws Exception {
        HttpRequest request = req.getRequest();
        Order order = (Order) req.getBody();
        System.out.println("Http server receive request : "+ FastJsonUtil.bean2Json(order));
        dobusiness(order);
        System.out.println("服务端修改内容后为:  "+FastJsonUtil.bean2Json(order));
        ChannelFuture f = ctx.writeAndFlush(new HttpXmlResponse(null, order));
        if (request.headers().get(CONNECTION) != KEEP_ALIVE){
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    ctx.close();
                }
            });
        }
    }

    private void dobusiness(Order order){
        order.getCustomer().setFirstName("狄");
        order.getCustomer().setLastName("仁杰");
        List<String> midNames = new ArrayList<>();
        midNames.add("李元芳");
        order.getCustomer().setMiddleNames(midNames);
        Address address = order.getBillTo();
        address.setCity("洛阳");
        address.setCountry("大唐");
        address.setState("河南道");
        address.setPortCode("123456");
        order.setBillTo(address);
        order.setShipTo(address);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()){
            sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
