package syl.custom;


/**
 * Netty协议栈使用到的数据结构
 *
 * @author 史彦磊
 * @create 2018-04-13 15:51.
 */
public final class NettyMessage {

    private Header header;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }


}
