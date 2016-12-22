package cn.sczhckj.order.data.listener;

/**
 * @ describe:  WebSocket监听
 * @ author: Like on 2016/12/21.
 * @ email: 572919350@qq.com
 */

public interface OnWebSocketListenner {

    void onBinaryMessage(byte[] payload);

    void onClose(int code, String reason);

    void onOpen();

    void onRawTextMessage(byte[] payload);

    void onTextMessage(String payload);

}
