package cn.sczhckg.order.data.event;

import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;

/**
 * @describe: 结账类型
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class SettleAountsTypeEvent {
    /**
     * 优惠
     */
    public final static int FTYPE = 0;
    /**
     * 支付
     */
    public final static int PTYPE = 1;
    /**
     * 打赏
     */
    public final static int GTYPE = 2;
    /**
     * 结账清单
     */
    public final static int TTYPE = 3;
    /**
     * 评价
     */
    public final static int ETYPE = 4;

    private int type;

    private FavorableTypeBean favorableTypeBean;

    private PayTypeBean payTypeBean;

    public SettleAountsTypeEvent(int type) {
        this.type = type;
    }

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
