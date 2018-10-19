package syl.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import syl.custom.codec.NettyMarshallingEncoder;

import java.util.Map;

/**
 * Netty消息编码类
 *
 * @author 史彦磊
 * @create 2018-04-13 15:59.
 */
public final class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    NettyMarshallingEncoder encoder;

    public NettyMessageEncoder() {
        this.encoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }



    @Override
    protected  void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null){
            throw new Exception("需要编码的消息为空");
        }
        Header header = msg.getHeader();
//        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(header.getCrcCode());
        sendBuf.writeInt(header.getLength());
        sendBuf.writeLong(header.getSessionID());
        sendBuf.writeByte(header.getType());
        sendBuf.writeByte(header.getPriority());
        sendBuf.writeInt(header.getAttachment().size());


        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : header.getAttachment().entrySet()) {
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
            this.encoder.encode(ctx, msg.getBody(),sendBuf);
        }else{
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4,sendBuf.readableBytes());
    }
}
