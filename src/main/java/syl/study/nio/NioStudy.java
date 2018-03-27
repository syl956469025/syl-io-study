package syl.study.nio;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * nio 学习
 * 字符集：
 *  编码器将一系列字符编码成字节
 *  解码器将一系列字节解码为字符，
 *
 * @author 史彦磊
 * @create 2018-03-13 17:59.
 */
public class NioStudy {
    static String path = "d://shiyl";
    static Integer count = 0;
    static String str = "==========================张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三张三";

    static class MyVistor extends SimpleFileVisitor<Path>{

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                throws IOException
        {
            System.out.println(file.toString());
            return FileVisitResult.CONTINUE;
        }
    }

    public static void main(String[] args)  throws IOException, URISyntaxException{
        Path path = Files.walkFileTree(Paths.get(NioStudy.path), new MyVistor());

    }

    private static void filterDir() throws IOException {
        try(DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(path), path -> {
            if (path.endsWith("java")){
                return true;
            }
            return false;
        })){

            for (Path p : dir) {
                System.out.println(p.toString());
            }
        }
    }

    private static void writeFileByIo() throws IOException {
        Path path = Paths.get(NioStudy.path);
        try(OutputStream os = Files.newOutputStream(path, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING)){
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(str);
            bw.flush();
        }
    }

    private static void readFileByIo() throws IOException {
        Path path = Paths.get(NioStudy.path);
        try(InputStream is = Files.newInputStream(path, StandardOpenOption.READ)){
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String count = null;
            do {
                count = br.readLine();
                if (count !=null){
                    System.out.println(count);
                }
            }while (count !=null);
        }
    }

    private static void writeMapFile() throws IOException {
        Path path = Paths.get(NioStudy.path);
        byte[] bytes = str.getBytes();
        try (SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.READ)){
            FileChannel fchan = (FileChannel)channel;
            MappedByteBuffer map = fchan.map(FileChannel.MapMode.READ_WRITE, channel.size(), bytes.length);
            map.put(bytes);
        }
    }

    private static void writeFile() throws IOException {
        Path path = Paths.get(NioStudy.path);
        try (SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE,StandardOpenOption.WRITE)){
            byte[] bytes = str.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            for (int i = 0; i < 3; i++) {
                buffer.put(bytes);
                buffer.rewind();
                channel.write(buffer);
                buffer.rewind();
            }
        }
    }


    public static void mapFile()throws IOException{
        Path path = Paths.get(NioStudy.path);
        try(SeekableByteChannel seekableByteChannel = Files.newByteChannel(path, StandardOpenOption.READ)){
            FileChannel fchan = (FileChannel)seekableByteChannel;
            long size = fchan.size();
            System.out.println(size);
            MappedByteBuffer map = fchan.map(FileChannel.MapMode.READ_ONLY, 0, size);
            for (int i = 0; i < size; i++) {
                System.out.println((char)map.get());
            }
        }
    }



    public static void readFile()throws IOException, URISyntaxException{
        Path path = Paths.get(NioStudy.path);
        try(SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.READ)){
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = -1;
            do {
                read = channel.read(buffer);
                if (read !=-1){
                    buffer.rewind();
                }
                System.out.println("========================================="+read);
                for (int i = 0; i < read; i++) {
                    System.out.println((char)buffer.get());
                }
                count ++;
            }while (read !=-1);
            System.out.println(count);

        }
    }


}
