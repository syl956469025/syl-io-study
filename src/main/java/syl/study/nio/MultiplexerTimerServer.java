package syl.study.nio;

import io.netty.util.internal.StringUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用器时间服务
 *
 * @author 史彦磊
 * @create 2018-03-15 16:30.
 */
public class MultiplexerTimerServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverChannel;

    private volatile boolean stop;


    /**
     * 初始化多路复用器、绑定监听端口
     * @param port
     */
    public MultiplexerTimerServer(int port){
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port),1024);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        this.stop = true;
    }




    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                         handleInput(key);
                    }catch (Exception e){
                        e.printStackTrace();
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (selector != null){
            try {
                selector.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()){
            //处理新接入的请求消息
            if (key.isAcceptable()){
                //接入一个新的连接
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector,SelectionKey.OP_READ);
            }
            if (key.isReadable()){
                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int read = sc.read(readBuffer);
                System.out.println("读入"+read+"个字节");
                if (read>0){
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("服务端发过来的内容为:"+body);
                    String currentTime = "QUERY TIME ORDER".equals(body) ? LocalDateTime.now().toString():"BAD ORDER";
                    System.out.println(currentTime);
                    doWrite(sc,currentTime);
                }else if (read < 0){
                    // 对端链路关闭
                    key.cancel();
                    sc.close();
                }else{
                    //读取到0个字节
                    System.out.println("读取到0个字节 忽略");
                }
            }
        }
    }


    private void doWrite(SocketChannel channel,String response) throws IOException {
        if (!StringUtil.isNullOrEmpty(response)){
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
            System.out.println("发送到客户端成功");
        }
    }

}
