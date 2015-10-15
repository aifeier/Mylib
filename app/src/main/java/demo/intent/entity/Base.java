package demo.intent.entity;

import java.io.Serializable;

/**
 * Created by n-240 on 2015/9/24.
 */
public class Base implements Serializable {
    private static final long serialVersionID = 1l;


    private Integer errNum;

    private String retMsg;

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Integer getErrNum() {
        return errNum;
    }

    public void setErrNum(Integer errNum) {
        this.errNum = errNum;
    }
}
