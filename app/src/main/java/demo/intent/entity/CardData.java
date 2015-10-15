package demo.intent.entity;

import java.io.Serializable;

/**
 * Created by n-240 on 2015/9/23.
 */
public class CardData implements Serializable{

    private static final long serialVersionUID = 1l;

    private String sex;

    private String birthday;

    private String address;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
