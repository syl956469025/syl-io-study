package syl.study.nio;

/**
 * NIO 时间服务器 服务端
 *
 * @author 史彦磊
 * @create 2018-03-15 16:28.
 */
public class TimeServer {


    public static void main(String[] args) {
        int port = 8080;
        MultiplexerTimerServer timeServer = new MultiplexerTimerServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }



}
