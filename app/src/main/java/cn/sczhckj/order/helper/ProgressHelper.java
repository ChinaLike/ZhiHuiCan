package cn.sczhckj.order.helper;

import java.io.IOException;

import cn.sczhckj.order.data.bean.ProgressBean;
import cn.sczhckj.order.data.listener.OnProgressListener;
import cn.sczhckj.order.overwrite.ProgressResponseBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * @describe:
 * @author: Like on 2016/12/7.
 * @Email: 572919350@qq.com
 */

public class ProgressHelper {
    private static ProgressBean progressBean = new ProgressBean();
    private static ProgressHandler mProgressHandler;

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        final OnProgressListener progressListener = new OnProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                if (mProgressHandler == null) {
                    return;
                }
                progressBean.setBytesRead(progress);
                progressBean.setContentLength(total);
                progressBean.setDone(done);
                mProgressHandler.sendMessage(progressBean);
            }
        };

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener)).build();
            }
        });

        return builder;
    }

    public static void setProgressHandler(ProgressHandler progressHandler){
        mProgressHandler = progressHandler;
    }

}
