package syl.study.netty.productserver;

import java.io.Serializable;

/**
 * @author 史彦磊
 * @create 2018-03-19 10:38.
 */
public class SubscribeResp implements Serializable {


    private int subReqID;

    private int respCode;

    private String desc;

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
