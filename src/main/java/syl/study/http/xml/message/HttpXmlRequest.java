package syl.study.http.xml.message;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author 史彦磊
 * @create 2018-03-23 17:52.
 */
public class HttpXmlRequest {
    private FullHttpRequest request;
    private Object body;

    public HttpXmlRequest(FullHttpRequest ctx, Object body) {
        this.request = ctx;
        this.body=body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpXmlRequest [request=" + request + ", body =" + body + "]";
    }
}
