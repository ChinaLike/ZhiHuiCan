package cn.sczhckj.order.helper;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.sczhckj.order.data.bean.device.ProgressBean;

/**
 * @describe:
 * @author: Like on 2016/12/7.
 * @Email: 572919350@qq.com
 */

public abstract class ProgressHandler {

    protected abstract void sendMessage(ProgressBean progressBean);

    protected abstract void handleMessage(Message message);

    protected abstract void onProgress(long progress, long total, boolean done);

    protected static class ResponseHandler extends Handler {

        private ProgressHandler mProgressHandler;

        public ResponseHandler(ProgressHandler mProgressHandler, Looper looper) {
            super(looper);
            this.mProgressHandler = mProgressHandler;
        }

        @Override
        public void handleMessage(Message msg) {
            mProgressHandler.handleMessage(msg);
        }
    }

}
