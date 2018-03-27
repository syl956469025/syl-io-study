package syl.study.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * bio 客户端代码
 *
 * @author 史彦磊
 * @create 2018-03-14 17:40.
 */
public class BioClient {


    public static void main(String[] args) throws IOException {
        System.out.println("客户端1启动");
        System.out.println("当输入 end 时，客户端将终止");
        Socket server=null;
        BufferedReader br=null;
        PrintWriter pw =null;
        BufferedReader in=null;
        while (true){
            server = new Socket("127.0.0.1",9999);
            br = new BufferedReader(new InputStreamReader(System.in));
            pw = new PrintWriter(server.getOutputStream());
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String s = br.readLine();
            pw.print(s);
            pw.flush();
            if ("end".equals(s)){
                System.out.println("客户端将关闭连接");
                break;
            }
            System.out.println("服务端： "+in.readLine());
        }
        in.close();
        pw.close();
        br.close();
        server.close();
    }



}
