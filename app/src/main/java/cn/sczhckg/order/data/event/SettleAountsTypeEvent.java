package cn.sczhckg.order.data.event;

import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;

/**
 * @describe: 结账类型
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class SettleAountsTypeEvent {

    public final static int FTYPE=0;

    public final static int PTYPE=1;

    private int type;

    private FavorableTypeBean favorableTypeBean;

    private PayTypeBean payTypeBean;

    public SettleAountsTypeEvent(int type, FavorableTypeBean favorableTypeBean) {
        this.type = type;
        this.favorableTypeBean = favorableTypeBean;
    }

    public SettleAountsTypeEvent(PayTypeBean payTypeBean, int type) {
        this.payTypeBean = payTypeBean;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FavorableTypeBean getFavorableTypeBean() {
        return favorableTypeBean;
    }

    public void setFavorableTypeBean(FavorableTypeBean favorableTypeBean) {
        this.favorableTypeBean = favorableTypeBean;
    }

    public PayTypeBean getPayTypeBean() {
        return payTypeBean;
    }

    public void setPayTypeBean(PayTypeBean payTypeBean) {
        this.payTypeBean = payTypeBean;
    }
}
