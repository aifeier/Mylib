package demo.intent.entity;

import java.io.Serializable;

/**
 * Created by n-240 on 2015/10/28.
 */
public class NewsInfo implements Serializable{
    private String hottime;

    private String title;

    private String description;

    private String picUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHottime() {
        return hottime;
    }

    public void setHottime(String hottime) {
        this.hottime = hottime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private String url;
}
