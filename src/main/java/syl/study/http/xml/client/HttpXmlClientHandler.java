package syl.study.http.xml.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import syl.study.http.xml.OrderFactory;
import syl.study.http.xml.message.HttpXmlRequest;
import syl.study.http.xml.message.HttpXmlResponse;

/**
 * @author 史彦磊
 * @create 2018-03-26 16:20.
 */
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));
        ctx.writeAndFlush(request);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("接收到服务端的消息头为:"+msg.getHttpResponse().headers().names());
        System.out.println("接收到服务端的消息体为:"+msg.getResult());
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("==============================");
        cause.printStackTrace();
        ctx.close();
    }

}
