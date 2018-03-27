package syl.study.http.xml.message;

import com.thoughtworks.xstream.XStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.IBindingFactory;
import syl.study.http.xml.Address;
import syl.study.http.xml.Customer;
import syl.study.http.xml.Order;
import syl.study.http.xml.Shipping;

import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * @author 史彦磊
 * @create 2018-03-23 17:25.
 */
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T>{

    private IBindingFactory factory;
    private StringReader reader;
    private Class<?> clazz;
    private boolean isPrint;
    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected  AbstractHttpXmlDecoder(Class<?> clazz){
        this(clazz,false);
    }

    public AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }


    protected Object decode0(ChannelHandlerContext ctx, ByteBuf body)throws Exception{
        String content = body.toString(UTF_8);
        if (isPrint){
            System.out.println("消息体为: "+content);
        }
        XStream xs = new XStream();
        xs.setMode(XStream.NO_REFERENCES);
        xs.processAnnotations(new Class[]{Order.class, Customer.class, Shipping.class,Address.class});
        Object o = xs.fromXML(content);
        return o;
    }


}
