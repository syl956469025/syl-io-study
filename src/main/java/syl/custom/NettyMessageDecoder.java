package syl.custom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;
import syl.custom.codec.NettyMarshallingDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 解码器
 *
 * @author 史彦磊
 * @create 2018-04-13 17:48.
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder  {

    private Logger logger  = Logger.getLogger(this.getClass());

    NettyMarshallingDecoder decoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.decoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null){
            logger.info("接收到的消息为空");
            return null;
        }
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionID(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        int size = frame.readInt();
        if (size >0){
            Map<String, Object> attch = new HashMap<>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();
                keyArray = new byte[keySize];
                in.readBytes(keyArray);
                key = new String(keyArray,"UTF-8");
                attch.put(key,decoder.decode(ctx,frame));
            }
            keyArray = null;
            key = null;
            header.setAttachment(attch);
        }
        if (frame.readableBytes() > 4){
            msg.setBody(decoder.decode(ctx,frame));
        }
        msg.setHeader(header);
        return msg;
    }
}
