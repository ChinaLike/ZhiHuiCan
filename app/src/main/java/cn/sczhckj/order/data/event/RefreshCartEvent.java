package cn.sczhckj.order.data.event;

import java.util.List;

import cn.sczhckj.order.data.bean.FoodBean;

/**
 * @describe: 刷新购物车事件
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class RefreshCartEvent {

    private FoodBean bean;

    private List<FoodBean> beanList;

    public RefreshCartEvent(FoodBean bean) {
        this.bean = bean;
    }

    public RefreshCartEvent(List<FoodBean> beanList) {
        this.beanList = beanList;
    }

    public FoodBean getBean() {
        return bean;
    }

    public void setBean(FoodBean bean) {
        this.bean = bean;
    }

    public List<FoodBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<FoodBean> beanList) {
        this.beanList = beanList;
    }
}
