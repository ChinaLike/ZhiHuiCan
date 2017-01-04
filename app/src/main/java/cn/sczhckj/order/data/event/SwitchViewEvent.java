package cn.sczhckj.order.data.event;

import java.util.List;

import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.bean.food.FoodBean;

/**
 * @ describe: 界面切换事件
 * @ author: Like on 2016/12/26.
 * @ email: 572919350@qq.com
 */

public class SwitchViewEvent {
    /**
     * 点菜
     */
    public static final int BOTTOM_ORDER = 0;
    /**
     * 服务
     */
    public static final int BOTTOM_SERVICE = 1;
    /**
     * 结账
     */
    public static final int BOTTOM_BILL = 2;
    /**
     * 进入菜品详情
     */
    public static final int DISHES_DETAILS_IN = 3;
    /**
     * 退出菜品详情
     */
    public static final int DISHES_DETAILS_OUT = 4;
    /**
     * 更多优惠
     */
    public static final int FAVORABLE = 5;
    /**
     * 更多优惠退出
     */
    public static final int FAVORABLE_OUT = 6;
    /**
     * 办卡
     */
    public static final int FAVORABLE_CARD = 7;
    /**
     * 主页显示
     */
    public static final int MAIN = 8;

    private int type;

    private int orderType;

    private FoodBean bean;

    private List<FoodBean> beanList;

    private CardInfoBean.Card card;

    public SwitchViewEvent(int type) {
        this.type = type;
    }

    public SwitchViewEvent(int type, FoodBean bean) {
        this.type = type;
        this.bean = bean;
    }

    public SwitchViewEvent(int type, int orderType) {
        this.type = type;
        this.orderType = orderType;
    }

    public SwitchViewEvent(List<FoodBean> beanList, int type) {
        this.beanList = beanList;
        this.type = type;
    }

    public SwitchViewEvent(int type, FoodBean bean, List<FoodBean> beanList) {
        this.type = type;
        this.bean = bean;
        this.beanList = beanList;
    }

    public SwitchViewEvent(int type, CardInfoBean.Card card) {
        this.type = type;
        this.card = card;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public CardInfoBean.Card getCard() {
        return card;
    }

    public void setCard(CardInfoBean.Card card) {
        this.card = card;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
