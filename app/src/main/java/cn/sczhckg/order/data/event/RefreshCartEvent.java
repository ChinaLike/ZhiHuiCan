package cn.sczhckg.order.data.event;

import cn.sczhckg.order.data.bean.DishesBean;

/**
 * @describe: 刷新购物车事件
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class RefreshCartEvent {

    private DishesBean bean;

    public RefreshCartEvent(DishesBean bean) {
        this.bean = bean;
    }

    public DishesBean getBean() {
        return bean;
    }

    public void setBean(DishesBean bean) {
        this.bean = bean;
    }
}
