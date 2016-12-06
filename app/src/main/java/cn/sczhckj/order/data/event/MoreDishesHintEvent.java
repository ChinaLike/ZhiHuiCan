package cn.sczhckj.order.data.event;

/**
 * @describe: 菜品过多提示数量
 * @author: Like on 2016/11/21.
 * @Email: 572919350@qq.com
 */

public class MoreDishesHintEvent {

    private int number;

    public MoreDishesHintEvent(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
