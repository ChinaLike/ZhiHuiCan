package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.data.bean.push.HeartBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.AppSystemUntil;
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

    /**
     * 是否链接
     */
    private boolean isConnect = false;

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
        timer = new Timer();
        mWebSocket.connect(Config.URL_HEART_SERVICE + AppSystemUntil.getOriginalAndroidID(getApplicationContext()), this);
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x123:
                    mWebSocket.reConnection();
                    break;
                case 0x122:
                    String msgS = msg();
                    mWebSocket.sendMessage(msgS);
                    break;
            }
        }
    };

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mWebSocket != null){
                    if (isConnect){
                        /**如果连接，发送心跳*/
                        mHandler.sendEmptyMessage(0x122);
                    }else {
                        /**没有连接，重新连接*/
                        mHandler.sendEmptyMessage(0x123);
                    }
                }
            }
        }, 100, TIME);
    }


    /**
     * 发送消息内容
     *
     * @return
     */
    private String msg() {
        HeartBean bean = new HeartBean();
        bean.setDeviceId(AppSystemUntil.getOriginalAndroidID(getApplicationContext()));
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
        isConnect = false;
    }

    @Override
    public void onOpen() {
        isConnect = true;
    }

    @Override
    public void onRawTextMessage(byte[] payload) {
    }

    @Override
    public void onTextMessage(String payload) {
    }

}
