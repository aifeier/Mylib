package demo.intent.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by n-240 on 2015/10/28.
 */
public class News implements Serializable{

    private Integer code ;
    private String msg  ;

    public ArrayList<NewsInfo> getNewslist() {
        return newslist;
    }

    public void setNewslist(ArrayList<NewsInfo> newslist) {
        this.newslist = newslist;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ArrayList<NewsInfo> newslist;

}
