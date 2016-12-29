package cn.sczhckj.order.data.event;

/**
 * @ describe:  刷新界面事件
 * @ author: Like on 2016/12/29.
 * @ email: 572919350@qq.com
 */

public class RefreshViewEvent {

    /**
     * 消费中
     */
    public static final int CONSUMING = 0;
    /**
     * 结账中
     */
    public static final int BILLING = 1;

    private int type;

    public RefreshViewEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
