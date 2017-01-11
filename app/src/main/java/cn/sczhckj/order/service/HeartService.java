package cn.sczhckj.order.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.sczhckj.order.Config;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;

/**
 * @ describe:  心跳检测
 * @ author: Like on 2017/1/8.
 * @ email: 572919350@qq.com
 */

public class HeartService extends Service implements OnWebSocketListenner {

    private final int TIME = 60 * 1000;

    private WebSocketImpl mWebSocket = new WebSocketImpl();

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
        mWebSocket.push(Config.URL_HEART_SERVICE + AppSystemUntil.getAndroidID(getApplicationContext()), this);
        sendMessage(mWebSocket);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 间隔发送消息检测心跳
     */
    private void sendMessage(final WebSocketImpl mWebSocket) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String msg = msg();
                mWebSocket.sendMessage(msg);
            }
        }, 100, TIME);
    }

    /**
     * 发送消息内容
     *
     * @return
     */
    private String msg() {
        long time = System.currentTimeMillis();
        JSONObject object = new JSONObject();
        try {
            object.put("op", OP.PUSH_HEART + "");
            object.put("time", time + "");
            JSONObject params = new JSONObject();
            params.put("deviceId", AppSystemUntil.getAndroidID(getApplicationContext()) + "");
            object.put("params", params);
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void onBinaryMessage(byte[] payload) {
    }

    @Override
    public void onClose(int code, String reason) {
        mWebSocket.reConnection();
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onRawTextMessage(byte[] payload) {
    }

    @Override
    public void onTextMessage(String payload) {
    }
}
