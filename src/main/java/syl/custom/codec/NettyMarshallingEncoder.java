package syl.custom.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 * 编码器
 *
 * @author 史彦磊
 * @create 2018-04-13 17:21.
 */
public class NettyMarshallingEncoder extends MarshallingEncoder {


    public NettyMarshallingEncoder(MarshallerProvider provider) {
        super(provider);
    }


    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
