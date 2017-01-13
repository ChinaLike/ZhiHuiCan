package cn.sczhckj.order.data.event;

import cn.sczhckj.order.data.bean.push.PushCommonBean;

/**
 * @ describe:  WebSocket推送
 * @ author: Like on 2016/12/21.
 * @ email: 572919350@qq.com
 */

public class WebSocketEvent {
    /**
     * 菜品完成
     */
    public static final int TYPE_FOOD_ARRIVE = 0;
    /**
     * 服务完成
     */
    public static final int TYPE_SERVICE_COMPLETE = 1;
    /**
     * 锁定界面
     */
    public static final int TYPE_LOCK = 2;
    /**
     * 解锁界面
     */
    public static final int TYPE_UNLOCK = 3;
    /**
     * 结账完成
     */
    public static final int TYPE_BILL_FINISH = 4;
    /**
     * 检测WebSocket是否链接
     */
    public static final int CHECK_WEBSOCKET = 5;
    /**
     * 初始化成功
     */
    public static final int INIT_SUCCESS = 6;

    private PushCommonBean bean;

    private int type;

    private String message;

    public WebSocketEvent(int type) {
        this.type = type;
    }

    public WebSocketEvent(int type, String message) {
        this.message = message;
        this.type = type;
    }

    public WebSocketEvent(int type, PushCommonBean bean) {
        this.bean = bean;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PushCommonBean getBean() {
        return bean;
    }

    public void setBean(PushCommonBean bean) {
        this.bean = bean;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
