package cn.sczhckg.order.data.event;

import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;

/**
 * @describe: 评价清单
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class EvaluateListEvent {

    public final static int ALL=1;

    public final static int ITEM=0;

    private int tag;

    private SettleAccountsDishesItemBean bean;

    public EvaluateListEvent(int tag) {
        this.tag = tag;
    }

    public EvaluateListEvent(int tag, SettleAccountsDishesItemBean bean) {
        this.tag = tag;
        this.bean = bean;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public SettleAccountsDishesItemBean getBean() {
        return bean;
    }

    public void setBean(SettleAccountsDishesItemBean bean) {
        this.bean = bean;
    }
}
