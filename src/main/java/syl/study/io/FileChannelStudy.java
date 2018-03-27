package syl.study.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 史彦磊
 * @create 2018-03-12 14:39.
 */
public class FileChannelStudy {


    public static void main(String[] args) throws IOException {

        FileChannelStudy s = new FileChannelStudy();
        s.FileChannelRead();
    }

    public void FileChannelRead() throws IOException {
        RandomAccessFile file = new RandomAccessFile("d://test.txt","rw");
        FileChannel channel = file.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate(48);
        int read = channel.read(allocate);
        while (read != -1){
            System.out.println("Read" + read);
            allocate.flip();
            while (allocate.hasRemaining()){
                System.out.println((char)allocate.get());
            }
            allocate.clear();
            read = channel.read(allocate);
        }
        file.close();
    }




}
