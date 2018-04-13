package syl.custom.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * 解码器
 *
 * @author 史彦磊
 * @create 2018-04-13 17:32.
 */
public class NettyMarshallingDecoder extends MarshallingDecoder {


    public NettyMarshallingDecoder(UnmarshallerProvider provider,int maxObjectSize) {
        super(provider,maxObjectSize);
    }

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
