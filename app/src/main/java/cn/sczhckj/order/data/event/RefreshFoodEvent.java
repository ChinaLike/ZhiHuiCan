package cn.sczhckj.order.data.event;

import java.util.List;

import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @describe: 菜品刷新事件
 * @author: Like on 2016/11/16.
 * @Email: 572919350@qq.com
 */

public class RefreshFoodEvent {
    /**
     * 菜品列表发起的加菜
     */
    public static final int ADD_FOOD = 0;
    /**
     * 菜品列表发起的减菜
     */
    public static final int MINUS_FOOD = 1;
    /**
     * 数据提交了
     */
    public static final int CART_COMMIT = 2;
    /**
     * 退菜
     */
    public static final int CART_REFUND = 3;
    /**
     * 购物车发起的减菜
     */
    public static final int CART_MINUS_FOOD = 4;
    /**
     * 详情发起的减菜
     */
    public static final int DETAILS_MINUS_FOOD = 5;
    /**
     * 详情发起的加菜
     */
    public static final int DETAILS_ADD_FOOD = 6;
    /**
     * 点赞
     */
    public static final int FAVOR_FOOD = 7;


    private FoodBean bean;

    private List<FoodBean> beanList;

    private int type;

    private int from;

    public RefreshFoodEvent(int type) {
        this.type = type;
    }

    public RefreshFoodEvent(int type, FoodBean bean) {
        this.bean = bean;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
