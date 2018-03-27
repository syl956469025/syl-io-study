package syl.study.io;

import java.io.*;

/**
 * 学习流
 *
 * @author 史彦磊
 * @create 2018-03-12 11:44.
 */
public class StreamStudy {


    public static void main(String[] args) throws IOException {
//        StreamStudy stream = new StreamStudy();
//        stream.readFileQuick("D:\\home\\mtime\\logs\\cmc.ex.voucher.repair.task/default.log");
//        stream.writeFile("d://task.txt",s);
        String msg = "hello,world!";
        byte[] bytes = msg.getBytes();
        ByteArrayInputStream array = new ByteArrayInputStream(bytes);
        int result = -1;
        while ((result = array.read())!=-1){
            System.out.println((char)result);
        }
        array.close();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(bytes);
        FileOutputStream fos = new FileOutputStream("d://task.txt");
        out.writeTo(fos);
        fos.flush();
        out.close();



    }


    public String readFile(String filePath) throws IOException {
        // 根据path路径实例化一个输入流对象
        File file  = new File(filePath);
        System.out.println(file.getParent());
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        // 返回这个输入流中可以被读取的剩下的bytes字节的估计值
        int size = fis.available();
        System.out.println(size);
        // 根据输入流中的字节数创建byte数组
        byte[] array = new byte[size];
        // 把数据读到数组中
        fis.read(array);
        fis.close();
        return new String(array);
    }

    public void readFileQuick(String filePath) throws IOException {
        // 根据path路径实例化一个输入流对象
        File file  = new File(filePath);
        System.out.println(file.getParent());
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String s = "";
        while ((s=br.readLine())!= null){
            System.out.println(s);
        }
        br.close();
    }


    public void writeFile(String filePath,String content) throws IOException {
        // 根据路径创建输出流对象
        FileOutputStream fos = new FileOutputStream(filePath);
        // 把String转化为byte数组
        byte [] array = content.getBytes();
        fos.write(array);
        fos.close();
    }


}
