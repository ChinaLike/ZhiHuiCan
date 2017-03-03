package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.data.bean.push.HeartBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.websocket.WebSocket;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;

/**
 * @ describe:  心跳检测
 * @ author: Like on 2017/1/8.
 * @ email: 572919350@qq.com
 */

public class HeartService extends Service implements OnWebSocketListenner {

    private final int TIME = 20 * 1000;

    private WebSocketImpl mWebSocket = new WebSocketImpl();

    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mWebSocket.connect(Config.URL_HEART_SERVICE + AppSystemUntil.getHeartAndroidID(getApplicationContext()), this);
        sendMessage();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 定时重新发起连接
     */
    private void reConnection(final WebSocketImpl mWebSocket) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mWebSocket.reConnection();
            }
        }, 100, TIME);
    }

    /**
     * 间隔发送消息检测心跳
     */
    private void sendMessage() {
        Timer timer = new Timer();
        timer.schedule(mTimerTask, 100, TIME);
    }

    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            String msg = msg();
            if (mWebSocket != null)
                mWebSocket.sendMessage(msg);
        }
    };

    /**
     * 发送消息内容
     *
     * @return
     */
    private String msg() {
        HeartBean bean = new HeartBean();
        bean.setDeviceId(AppSystemUntil.getHeartAndroidID(getApplicationContext()));
        bean.setIp(AppSystemUntil.ip(getApplicationContext()));

        RestRequest<HeartBean> restRequest = JSONRestRequest.Builder.build(HeartBean.class)
                .op(OP.PUSH_HEART)
                .time()
                .bean(bean);

        return restRequest.toRequestString();
    }

    @Override
    public void onBinaryMessage(byte[] payload) {
    }

    @Override
    public void onClose(int code, String reason) {
        if (timer != null) {
            timer = new Timer();
        }
        reConnection(mWebSocket);
    }

    @Override
    public void onOpen() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onRawTextMessage(byte[] payload) {
    }

    @Override
    public void onTextMessage(String payload) {
    }

}
