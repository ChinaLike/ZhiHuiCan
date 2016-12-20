package cn.sczhckj.order.data.event;

import java.util.List;

import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @describe: 菜品刷新事件
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class RefreshFoodEvent {

    private FoodBean bean;

    private List<FoodBean> beanList;

    public RefreshFoodEvent(FoodBean bean) {
        this.bean = bean;
    }

    public RefreshFoodEvent(List<FoodBean> beanList) {
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
