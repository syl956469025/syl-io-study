package syl.custom.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import syl.custom.Header;
import syl.custom.MessageType;
import syl.custom.NettyMessage;
import syl.study.utils.FastJsonUtil;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证消息返回
 *
 * @author 史彦磊
 * @create 2018-04-13 18:29.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Logger logger  = Logger.getLogger(this.getClass());

    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<>();

    private String[] whiteList = {"127.0.0.1","192.168.89.46"};


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("接收到的握手消息为："+FastJsonUtil.bean2Json(msg));
        NettyMessage message = (NettyMessage) msg;
        //如果是握手应答消息，需要判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            logger.info("登录成功");
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildLoginResp((byte) -1);
            }else{
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for (String wip : whiteList) {
                    if (wip.equals(ip)){
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildLoginResp((byte)0):buildLoginResp((byte)-1);
                if (isOK){
                    nodeCheck.put(nodeIndex,true);
                }
            }
            logger.info("认证的返回消息为："+loginResp+" 消息体为： "+loginResp.getBody());
            ctx.writeAndFlush(loginResp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());//删除缓存
        ctx.close();
        ctx.fireChannelRead(cause);
    }

    private NettyMessage buildLoginResp(byte result){
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        msg.setHeader(header);
        msg.setBody(result);
        return msg;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
