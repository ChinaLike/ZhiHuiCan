package cn.sczhckj.order.data.event;

import cn.sczhckj.order.data.bean.FoodBean;

/**
 * @describe: 底部菜单按钮
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class BottomChooseEvent {

    private int type;

    private FoodBean bean;

    public BottomChooseEvent(int type, FoodBean bean) {
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

    public FoodBean getBean() {
        return bean;
    }

    public void setBean(FoodBean bean) {
        this.bean = bean;
    }
}
