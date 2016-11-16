package cn.sczhckg.order.data.event;

import cn.sczhckg.order.data.bean.DishesBean;

/**
 * @describe: 底部菜单按钮
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class BottomChooseEvent {

    private int type;

    private DishesBean bean;

    public BottomChooseEvent(int type, DishesBean bean) {
        this.type = type;
        this.bean = bean;
    }

    public BottomChooseEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DishesBean getBean() {
        return bean;
    }

    public void setBean(DishesBean bean) {
        this.bean = bean;
    }
}
