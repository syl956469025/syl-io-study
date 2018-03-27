package syl.study.http.xml.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import syl.study.http.xml.Order;
import syl.study.http.xml.message.AbstractHttpXmlDecoder;
import syl.study.http.xml.message.HttpXmlRequest;

import java.util.List;

import static com.sun.deploy.net.HttpRequest.CONTENT_TYPE;

/**
 * @author 史彦磊
 * @create 2018-03-23 17:16.
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest> {

    public HttpXmlRequestDecoder(Class<Order> orderClass) {
        this(orderClass,false);
    }
    public HttpXmlRequestDecoder(Class<Order> orderClass, boolean isPrint) {
        super(orderClass,isPrint);
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (!msg.decoderResult().isSuccess()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }
        HttpXmlRequest req = new HttpXmlRequest(msg, decode0(ctx, msg.content()));
        out.add(req);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE,"text/plain;charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
