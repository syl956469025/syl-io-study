package syl.study.http.xml.message;

import com.thoughtworks.xstream.XStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import syl.study.http.xml.Address;
import syl.study.http.xml.Customer;
import syl.study.http.xml.Order;
import syl.study.http.xml.Shipping;

import java.nio.charset.Charset;

/**
 * @author 史彦磊
 * @create 2018-03-23 18:11.
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {

    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected ByteBuf encode0(ChannelHandlerContext ctx,Object body) {
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);
        xs.processAnnotations(new Class[]{Order.class, Customer.class, Shipping.class,Address.class});
        String xml = xs.toXML(body);
        ByteBuf byteBuf = Unpooled.copiedBuffer(xml, UTF_8);
        return byteBuf;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("fail to encode");
        ctx.close();
    }
}
