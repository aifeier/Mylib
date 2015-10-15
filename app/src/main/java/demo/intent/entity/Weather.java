package demo.intent.entity;

/**
 * Created by n-240 on 2015/9/24.
 */
public class Weather extends Base{

    private static final long serialVersionID = 1l;

    public WeaherData getRetData() {
        return retData;
    }

    public void setRetData(WeaherData retData) {
        this.retData = retData;
    }

    private WeaherData retData;
}
