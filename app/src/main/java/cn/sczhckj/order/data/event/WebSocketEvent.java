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
     * 初始化成功
     */
    public static final int INIT_SUCCESS = 5;
    /**
     * 通知刷新菜品
     */
    public static final int REFRESH_FOOD = 6;
    /**
     * 通知刷新用户
     */
    public static final int REFRESH_USER = 7;
    /**
     * 通知刷新点菜记录
     */
    public static final int REFRESH_RECORD = 8;
    /**
     * 单桌点菜
     */
    public static final int ALONE_ORDER = 9;
    /**
     * 并桌
     */
    public static final int MERGE_TABLE = 10;
    /**
     * 版本检查
     */
    public static final int CHECK_VERSION = 11;
    /**
     * 通用Bean
     */
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
