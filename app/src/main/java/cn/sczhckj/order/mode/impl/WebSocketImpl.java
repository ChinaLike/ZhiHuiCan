package cn.sczhckj.order.mode.impl;

import android.util.Log;

import cn.sczhckj.order.data.listener.OnWebSocketListenner;
import cn.sczhckj.order.websocket.WebSocketConnection;
import cn.sczhckj.order.websocket.WebSocketConnectionHandler;
import cn.sczhckj.order.websocket.WebSocketException;

/**
 * @ describe:  WebSocket实现
 * @ author: Like on 2016/12/21.
 * @ email: 572919350@qq.com
 */

public class WebSocketImpl {

    private WebSocketConnection connection;


    public void WebSocketImpl() {

    }

    /**
     * 与服务端建立连接
     *
     * @param url                  通道地址
     * @param onWebSocketListenner 监听
     */
    public void push(String url, final OnWebSocketListenner onWebSocketListenner) {
        connection = new WebSocketConnection();
        try {
            connection.connect(url, new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    onWebSocketListenner.onOpen();
                }

                @Override
                public void onClose(int code, String reason) {
                    onWebSocketListenner.onClose(code, reason);
                }

                @Override
                public void onTextMessage(String payload) {
                    onWebSocketListenner.onTextMessage(payload);
                }

                @Override
                public void onRawTextMessage(byte[] payload) {
                    onWebSocketListenner.onRawTextMessage(payload);
                }

                @Override
                public void onBinaryMessage(byte[] payload) {
                    onWebSocketListenner.onBinaryMessage(payload);
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

}
