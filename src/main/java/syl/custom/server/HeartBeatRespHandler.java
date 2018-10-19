package syl.custom.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import syl.custom.Header;
import syl.custom.MessageType;
import syl.custom.NettyMessage;
import syl.study.utils.FastJsonUtil;

/**
 * 心跳消息回复处理器
 *
 * @author 史彦磊 yanlei.shi
 * @create 2018/4/17
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    private Logger logger  = Logger.getLogger(this.getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("接收到的心跳消息为："+FastJsonUtil.bean2Json(msg));
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()){
            logger.info("接收到心跳消息为:" + FastJsonUtil.bean2Json(message));
            NettyMessage headBeat = buildHeadBeat();
            logger.info("发送心跳回复消息给客户端：{}"+FastJsonUtil.bean2Json(headBeat));
            ctx.writeAndFlush(headBeat);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeadBeat(){
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        msg.setHeader(header);
        return msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.fireChannelRead(cause);
    }
}
