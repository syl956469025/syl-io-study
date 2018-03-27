package syl.study.http.xml.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import syl.study.http.xml.message.AbstractHttpXmlEncoder;
import syl.study.http.xml.message.HttpXmlResponse;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author 史彦磊
 * @create 2018-03-23 17:21.
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {




    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getResult());
        FullHttpResponse response = msg.getHttpResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, body);
        } else {
            response = new DefaultFullHttpResponse(msg.getHttpResponse().protocolVersion(),
                    msg.getHttpResponse().status(), body);
        }
        response.headers().set(CONTENT_TYPE, "text/xml");
        response.headers().setInt(CONTENT_LENGTH, body.readableBytes());
        out.add(response);
    }
}
