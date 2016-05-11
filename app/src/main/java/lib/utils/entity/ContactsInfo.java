package lib.utils.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by n-240 on 2016/5/11.
 * 联系人信息
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class ContactsInfo implements Serializable {

    private Long id;
    private String name;
    private String phone;
    /*排序关键词*/
    private String sortKey;
    /*头像*/
    private Bitmap bitmap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
