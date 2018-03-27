package syl.study.serial;

import syl.study.model.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author 史彦磊
 * @create 2018-03-16 18:08.
 */
public class JavaSerial {


    public static void main(String[] args) throws IOException {
        UserInfo user = new UserInfo();
        user.setUserID(100);
        user.setUserName("welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(user);
        os.flush();
        os.close();
        byte[] bytes = bos.toByteArray();
        System.out.println("JDK 序列化的长度为:"+ bytes.length);
        bos.close();
        System.out.println("=====================================");
        System.out.println("Buffer序列化长度为"+user.codeC().length);



    }





}
