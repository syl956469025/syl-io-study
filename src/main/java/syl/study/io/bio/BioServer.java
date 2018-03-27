package syl.study.io.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 传统bio服务端代码
 *
 * @author 史彦磊
 * @create 2018-03-14 17:27.
 */
public class BioServer {

    public static void main(String[] args) throws IOException {
        System.out.println("服务端启动...");
        ServerSocket server = new ServerSocket(9999);
        while (true){
            //阻塞等待
            Socket client = server.accept();
            InputStreamReader in = new InputStreamReader(client.getInputStream());
            PrintWriter pw = new PrintWriter(client.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int read = in.read();
            System.out.println("客户端: "+(char)read);
            pw.println(br.readLine());
            pw.flush();
            if ("end".equals(read)){
                System.out.println("服务器将关闭连接");
                break;
            }
        }

    }




}
