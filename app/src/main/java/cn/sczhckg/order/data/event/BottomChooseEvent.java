package cn.sczhckg.order.data.event;

/**
 * @describe: 底部菜单按钮
 * @author: Like on 2016/11/14.
 * @Email: 572919350@qq.com
 */

public class BottomChooseEvent {

    private int type;

    public BottomChooseEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
