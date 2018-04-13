package syl.custom.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import syl.custom.Header;
import syl.custom.MessageType;
import syl.custom.NettyMessage;

/**
 * 客户端
 *
 * @author 史彦磊
 * @create 2018-04-13 18:03.
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //如果是握手应答消息，需要判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            byte loginResult = (byte)message.getBody();
            if (loginResult != (byte)0){
                //握手失败，关闭连接
                ctx.close();
            }else{
                System.out.println("握手成功"+message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildLoginReq(){
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        msg.setHeader(header);
        return msg;
    }

}
