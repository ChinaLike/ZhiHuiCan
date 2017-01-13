package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.activity.InitActivity;
import cn.sczhckj.order.activity.LockActivity;
import cn.sczhckj.order.data.bean.push.PushCommonBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;

/**
 * @ describe: 开启WebSocket
 * @ author: Like on 2017-01-13.
 * @ email: 572919350@qq.com
 */

public class WebSocketService extends Service implements OnWebSocketListenner {
    /**
     * 锁屏WebSocket
     */
    private WebSocketImpl mWebSocketLock = new WebSocketImpl();
    /**
     * 菜品WebSocket
     */
    private WebSocketImpl mWebSocketFood = new WebSocketImpl();
    /**
     * 服务WebSocket
     */
    private WebSocketImpl mWebSocketService = new WebSocketImpl();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWebSocketLock.connect(Config.URL_LOCK_SERVICE + AppSystemUntil.getAndroidID(getApplicationContext()), this);
        mWebSocketFood.connect(Config.URL_FOOD_SERVICE + AppSystemUntil.getAndroidID(getApplicationContext()), this);
        mWebSocketService.connect(Config.URL_SERVICE_SERVICE + AppSystemUntil.getAndroidID(getApplicationContext()), this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void onClose(int code, String reason) {

    }

    @Override
    public void onOpen() {
        EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.INIT_SUCCESS));
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onTextMessage(String payload) {
        RestRequest<PushCommonBean> restRequest
                = JSONRestRequest.Parser.parse(payload, PushCommonBean.class);
        if (OP.PUSH_LOCK.equals(restRequest.getOp())) {
            /**锁定界面*/
            EventBus.getDefault().post(
                    new WebSocketEvent(WebSocketEvent.TYPE_LOCK, restRequest.getBean().getMessage()));
        } else if (OP.PUSH_UNLOCK.equals(restRequest.getOp())) {
            /**解锁界面*/
            EventBus.getDefault().post(
                    new WebSocketEvent(WebSocketEvent.TYPE_UNLOCK, restRequest.getBean()));
        } else if (OP.PUSH_BILL_FINISH.equals(restRequest.getOp())) {
            /**结账完成*/
            EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.TYPE_BILL_FINISH));
        }
    }
}