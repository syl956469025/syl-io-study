package syl.study.nio;

/**
 * nio 时间客服端
 *
 * @author 史彦磊
 * @create 2018-03-15 17:24.
 */
public class TimerClient {


    public static void main(String[] args) {
        int port = 8080;
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();
    }



}
