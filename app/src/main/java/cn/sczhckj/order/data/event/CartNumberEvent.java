package cn.sczhckj.order.data.event;

import cn.sczhckj.order.data.bean.FoodBean;

/**
 * @describe: 购物车数量变化事件
 * @author: Like on 2016/11/11.
 * @Email: 572919350@qq.com
 */

public class CartNumberEvent {

    private int tag;

    private int type;

    private FoodBean bean;

    public CartNumberEvent(int tag, int type, FoodBean bean) {
        this.tag = tag;
        this.type = type;
        this.bean = bean;
    }

    public int getTag() {
        return tag;
    }

    public int getType() {
        return type;
    }

    public FoodBean getBean() {
        return bean;
    }
}