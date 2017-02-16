package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.data.bean.push.PushCommonBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.websocket.WebSocket;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;

/**
 * @ describe: 开启WebSocket
 * @ author: Like on 2017-01-13.
 * @ email: 572919350@qq.com
 */

public class WebSocketService extends Service implements OnWebSocketListenner {
    /**
     * 数据刷新
     */
    private WebSocketImpl mWebSocketRefresh = new WebSocketImpl();
    /**
     * 初始化成功标志
     */
    private boolean isInit = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWebSocketRefresh.connect(Config.URL_NOTIFICATION_SERVICE + AppSystemUntil.getAndroidID(getApplicationContext()), this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void onClose(int code, String reason) {
        if (code == WebSocket.ConnectionHandler.CLOSE_CANNOT_CONNECT) {
            /**连接不能建立*/
            EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.INIT_FAIL));
        }
    }

    @Override
    public void onOpen() {
        if (!isInit) {
            isInit = true;
            EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.INIT_SUCCESS));
        }
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onTextMessage(String payload) {
        RestRequest<PushCommonBean> restRequest
                = JSONRestRequest.Parser.parse(payload, PushCommonBean.class);
        String op = restRequest.getOp();
        L.d("推送OP="+op);
        switch (op) {
            case OP.PUSH_LOCK:
                /**状态变更锁定屏幕*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.TYPE_LOCK, restRequest.getBean().getMessage()));
                break;
            case OP.PUSH_UNLOCK:
                /**状态变更解除屏幕锁定*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.TYPE_UNLOCK, restRequest.getBean()));
                break;
            case OP.PUSH_BILL_FINISH:
                /**结账完成*/
                EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.TYPE_BILL_FINISH));
                break;
            case OP.PUSH_REFRESH_FOOD:
                /**刷新菜品*/
                //需要刷新，菜品列表，已提交菜品，已点未提交菜品
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_FOOD));
                break;
            case OP.PUSH_REFRESH_RECORD:
                /**刷新点菜记录*/
                //需要刷新，已提交菜品，已点未提交菜品
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_RECORD));
                break;
            case OP.PUSH_ALONE_ORDER:
                /**单独点餐*/
                //将合并点菜标志设置为否，同时刷新菜品列表
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.ALONE_ORDER));
                break;
            case OP.PUSH_REFRESH_USER:
                /**变更会员信息*/
                //需要刷新，菜品，点菜记录
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_USER, restRequest.getBean()));

                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_FOOD));
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_RECORD));
                break;
            case OP.PUSH_MERGE_TABLE:
                /**并桌*/
                //需要刷新，会员信息，点菜记录，菜品，并桌信息
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.MERGE_TABLE));
                /**同时刷新菜品、刷新点菜记录*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_FOOD));
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_RECORD));
                break;
            case OP.PUSH_ARRIVE:
                /**出菜*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.TYPE_FOOD_ARRIVE, restRequest.getBean()));
                break;
            case OP.PUSH_CHECK_VERSION:
                /**版本检查*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.CHECK_VERSION));
                break;
            case OP.PUSH_COMPLETE:
                /**服务完成*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.TYPE_SERVICE_COMPLETE, restRequest.getBean()));
                break;
            default:
                /**其他*/
                break;
        }
    }
}
