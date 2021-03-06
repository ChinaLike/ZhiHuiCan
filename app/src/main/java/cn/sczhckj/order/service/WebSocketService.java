package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.data.bean.push.HeartBean;
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

    private final int TIME = 20 * 1000;

    private Timer timer;

    private boolean isConnect = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWebSocketRefresh.connect(Config.URL_NOTIFICATION_SERVICE + AppSystemUntil.getOriginalAndroidID(getApplicationContext()), this);
        return super.onStartCommand(intent, flags, startId);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x123:
                    mWebSocketRefresh.reConnection();
                    break;
            }
        }
    };

    /**
     * 定时重新发起连接
     */
    private void reConnection() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mWebSocketRefresh != null && !isConnect) {
                    mHandler.sendEmptyMessage(0x123);
                }
            }
        }, 100, TIME);
    }

    @Override
    public void onBinaryMessage(byte[] payload) {

    }

    @Override
    public void onClose(int code, String reason) {
        isConnect = false;
        L.d("测试" + code);
        if (code == WebSocket.ConnectionHandler.CLOSE_CANNOT_CONNECT) {
            /**连接不能建立*/
            EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.INIT_FAIL));
        } else {
            if (timer == null) {
                timer = new Timer();
            }
            reConnection();
        }
    }

    @Override
    public void onOpen() {
        isConnect = true;
        EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.INIT_SUCCESS));
    }

    @Override
    public void onRawTextMessage(byte[] payload) {

    }

    @Override
    public void onTextMessage(String payload) {
        RestRequest<PushCommonBean> restRequest
                = JSONRestRequest.Parser.parse(payload, PushCommonBean.class);
        String op = restRequest.getOp();
        mWebSocketRefresh.sendMessage(op);
        L.d("推送OP=" + op);
        switch (op) {
            case OP.PUSH_LOCK:
                /**状态变更-锁定屏幕*/
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.TYPE_LOCK, restRequest.getBean().getMessage()));
                break;
            case OP.PUSH_UNLOCK:
                /**状态变更-解除屏幕锁定(解锁后进入锁屏前界面)*/
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
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_USER, restRequest.getBean()));//刷新会员信息
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_FOOD));//刷新菜品，包含了刷新点菜记录
                break;
            case OP.PUSH_MERGE_TABLE:
                /**并桌*/
                //需要刷新，会员信息，点菜记录，菜品，并桌信息
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.MERGE_TABLE));//刷新并桌信息
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_FOOD));//刷新菜品，包含了刷新点菜记录
                EventBus.getDefault().post(
                        new WebSocketEvent(WebSocketEvent.REFRESH_USER, restRequest.getBean()));//刷新会员信息
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
            case OP.PUSH_EXCHANGE_TABLE:
                /**换桌*/
                EventBus.getDefault().post(new WebSocketEvent(WebSocketEvent.EXCHANGE_TABLE));
            default:
                /**其他*/
                break;
        }
    }
}
