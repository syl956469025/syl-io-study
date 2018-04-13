package syl.custom;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import syl.custom.codec.NettyMarshallingEncoder;

import java.util.List;
import java.util.Map;

/**
 * Netty消息编码类
 *
 * @author 史彦磊
 * @create 2018-04-13 15:59.
 */
public final class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    NettyMarshallingEncoder encoder;

    public NettyMessageEncoder() {
        this.encoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    protected  void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null){
            throw new Exception("需要编码的消息为空");
        }
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, org.omg.CORBA.Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            this.encoder.encode(ctx,value,sendBuf);
        }
        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() !=null){
            this.encoder.encode(ctx,msg.getHeader(),sendBuf);
        }else{
            sendBuf.writeInt(0);
            sendBuf.setInt(4,sendBuf.readableBytes());
        }
    }
}
