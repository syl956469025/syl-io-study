package syl.study.aio.client;

/**
 * @author 史彦磊
 * @create 2018-03-16 10:52.
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        new Thread(new AsyncTimeClientHandler("127.0.0.1",port),"AIO-AsyncTimeClientHandler-001").start();
    }
}
