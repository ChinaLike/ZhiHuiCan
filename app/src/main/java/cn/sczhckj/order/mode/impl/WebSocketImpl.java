package cn.sczhckj.order.mode.impl;

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
                    /**连接打开*/
                    onWebSocketListenner.onOpen();
                }

                @Override
                public void onClose(int code, String reason) {
                    /**链接关闭*/
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

    /**
     * 取消链接
     */
    public void disConnect() {
        if (connection != null) {
            connection.disconnect();
        }
    }

    /**
     * 发送消息
     *
     * @param text 消息内容
     */
    public void sendMessage(String text) {
        if (connection != null) {
            connection.sendTextMessage(text);
        }
    }

    /**
     * 重新连接
     */
    public void reConnection() {
        if (connection != null && !connection.isConnected()) {
            connection.reconnect();
        }
    }

}
