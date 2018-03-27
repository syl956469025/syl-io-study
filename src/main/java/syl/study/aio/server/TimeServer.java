package syl.study.aio.server;

/**
 * @author 史彦磊
 * @create 2018-03-15 18:27.
 */
public class TimeServer {


    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer,"AIO-AsyncTimeServerHandler-001").start();
    }


}
