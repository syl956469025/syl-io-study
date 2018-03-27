package syl.study.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * apache FileUtils 学习
 * @author 史彦磊
 * @create 2018-03-12 13:22.
 */
public class FileUtilStudy {

    public static void main(String[] args) throws IOException {
        FileUtilStudy f = new FileUtilStudy();
        f.read();
    }


    public void copy()throws IOException{
        File file1 = new File("D:\\\\home\\\\mtime\\\\logs\\\\cmc.ex.voucher.repair.task/default.log");
        File file2 = new File("d://test.txt");
        FileUtils.copyFile(file1,file2);
    }

    public void read() throws IOException {
        byte[] bytes = new byte[4];
        InputStream is = IOUtils.toInputStream("hello , world!", "UTF-8");
        int read = IOUtils.read(is, bytes);
        System.out.println(new String(bytes));
        is = IOUtils.toInputStream("hello , world!","UTF-8");
        byte[] bytess = new byte[14];
        IOUtils.read(is, bytess);
        System.out.println(new String(bytess));
    }






}
