package cn.sczhckg.order.data.event;

import java.util.List;

import cn.sczhckg.order.data.bean.FavorableTypeBean;
import cn.sczhckg.order.data.bean.PayTypeBean;
import cn.sczhckg.order.data.bean.SettleAccountsBean;

/**
 * @describe: 结账方式
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class SettleAountsCartEvent {
    /**加载数据*/
    public final static int LOADING=1;
    /**刷新数据*/
    public final static int REFRESH=2;

    private int type;

    private SettleAccountsBean bean;

    public SettleAountsCartEvent(int type, SettleAccountsBean bean) {
        this.type = type;
        this.bean = bean;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public SettleAccountsBean getBean() {
        return bean;
    }

    public void setBean(SettleAccountsBean bean) {
        this.bean = bean;
    }
}
