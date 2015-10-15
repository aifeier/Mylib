package demo.intent.entity;

/**
 * Created by n-240 on 2015/9/23.
 */
public class IDCard extends Base{
    private static final long serialVersionUID = 1L;

    private CardData retData;

    public CardData getRetData() {
        return retData;
    }

    public void setRetData(CardData retData) {
        this.retData = retData;
    }
}
