package cn.sczhckj.order.helper;

import android.os.Looper;
import android.os.Message;

import cn.sczhckj.order.data.bean.ProgressBean;

/**
 * @describe: 继承于ProgressHandler，用来发送和处理消息
 * @author: Like on 2016/12/7.
 * @Email: 572919350@qq.com
 */

public abstract class DownloadProgressHandler extends ProgressHandler {

    private static final int DOWNLOAD_PROGRESS = 1;
    protected ResponseHandler mHandler = new ResponseHandler(this, Looper.getMainLooper());

    @Override
    protected void sendMessage(ProgressBean progressBean) {
        mHandler.obtainMessage(DOWNLOAD_PROGRESS, progressBean).sendToTarget();

    }

    @Override
    protected void handleMessage(Message message) {
        switch (message.what) {
            case DOWNLOAD_PROGRESS:
                ProgressBean progressBean = (ProgressBean) message.obj;
                onProgress(progressBean.getBytesRead(), progressBean.getContentLength(), progressBean.isDone());
        }
    }
}
