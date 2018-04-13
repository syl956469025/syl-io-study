package syl.custom;

/**
 * @author 史彦磊
 * @create 2018-04-13 18:09.
 */
public enum MessageType {

    LOGIN_REQ((byte)1),
    LOGIN_RESP((byte)2);

    private byte value;

    MessageType(byte value){
        this.value = value;
    }

    public byte value(){
        return value;
    }


}
