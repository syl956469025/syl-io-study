package syl.study;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 史彦磊
 * @create 2018-03-13 13:55.
 */
public class Test {

    public static void main (String args[]) throws IOException {
        RandomAccessFile file = new RandomAccessFile("d://etc/global.conf","rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        System.out.println(read);
        buffer.flip();
        while (buffer.hasRemaining()){
            System.out.println((char)buffer.get());
        }

    }
}
