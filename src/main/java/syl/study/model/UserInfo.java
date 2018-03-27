package syl.study.model;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author 史彦磊
 * @create 2018-03-16 18:06.
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userName;

    private int userID;

    public UserInfo() {
    }

    public UserInfo(String userName, int userID) {
        this.userName = userName;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public byte[] codeC(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value=null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
