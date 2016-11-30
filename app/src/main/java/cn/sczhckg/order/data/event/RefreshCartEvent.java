package cn.sczhckg.order.data.event;

import java.util.List;

import cn.sczhckg.order.data.bean.DishesBean;

/**
 * @describe: 刷新购物车事件
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class RefreshCartEvent {

    private DishesBean bean;

    private List<DishesBean> beanList;

    public RefreshCartEvent(DishesBean bean) {
        this.bean = bean;
    }

    public RefreshCartEvent(List<DishesBean> beanList) {
        this.beanList = beanList;
    }

    public DishesBean getBean() {
        return bean;
    }

    public void setBean(DishesBean bean) {
        this.bean = bean;
    }

    public List<DishesBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<DishesBean> beanList) {
        this.beanList = beanList;
    }
}
