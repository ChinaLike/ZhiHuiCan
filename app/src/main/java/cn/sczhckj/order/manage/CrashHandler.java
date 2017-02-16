package cn.sczhckj.order.manage;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.device.ExceptionBean;
import cn.sczhckj.order.data.bean.device.VersionBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.until.AndroidUtils;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.CalendarUtils;
import cn.sczhckj.order.until.FileUtil;
import cn.sczhckj.order.until.NetUtils;
import cn.sczhckj.order.until.show.FL;
import cn.sczhckj.order.until.show.L;
import cn.sczhckj.order.until.show.T;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lyy on 2016/4/21.
 * 异常捕获
 */
final class CrashHandler implements Thread.UncaughtExceptionHandler, Callback<Bean<ResponseCommonBean>> {
    private static final Object LOCK = new Object();
    private static volatile CrashHandler INSTANCE = null;
    private static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private String mServerHost, mPramKey;
    private String mExceptionFileName = "AbsExceptionFile.crash";

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new CrashHandler(context);
            }
        }
        return INSTANCE;
    }

    private CrashHandler(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    /**
     * 开启异常捕获
     * 需要网络权限，get请求，异常参数
     *
     * @param serverHost 服务器地址
     * @param key        数据传输键值
     */
    public void setServerHost(String serverHost, String key) {
        mServerHost = serverHost;
        mPramKey = key;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //不要把线程都杀了，否则连日志都看不了
//            android.os.Process.killProcess(android.os.Process.myPid());
            //如果把这句话注释掉，有异常都不会退出
            System.exit(10);
        }
    }

    /**
     * 处理捕获到的异常
     *
     * @param ex
     * @return
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //在这里处理崩溃逻辑,将不再显示FC对话框
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                T.showLong(mContext, "很抱歉，程序出现异常，即将退出");
                Looper.loop();
            }
        }.start();
        sendExceptionInfo(ex);
        return true;
    }

    /**
     * 发送异常数据给服务器
     *
     * @param ex
     */
    private void sendExceptionInfo(final Throwable ex) {
        ExceptionBean info = new ExceptionBean();
        info.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        info.setType(Constant.VERSION_TYPE);
        info.setVersionCode(AndroidUtils.getVersionCode(mContext));
        info.setVersionName(AndroidUtils.getVersionName(mContext));
        info.setSystemVersionCode(Build.VERSION.SDK_INT + "");
        info.setPhoneMode(Build.MODEL);
        info.setExceptionMsg(FL.getExceptionString(ex));
        if (AndroidUtils.checkPermission(mContext, Manifest.permission.INTERNET) &&
                AndroidUtils.checkPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE)) {
            if (NetUtils.isConnected(mContext) && !TextUtils.isEmpty(mServerHost) && !TextUtils.isEmpty(mPramKey)) {
                RestRequest<ExceptionBean> restRequest = JSONRestRequest.Builder.build(ExceptionBean.class)
                        .op(OP.EXCEPTION)
                        .time()
                        .bean(info);
                /**进行数据校验*/
                Call<Bean<ResponseCommonBean>> vipCallBack = RetrofitRequest.service().exception(restRequest.toRequestString());
                vipCallBack.enqueue(this);
            }
        } else {
            L.e(TAG, "请在manifest文件定义android.permission.INTERNET和android.permission.ACCESS_NETWORK_STATE权限");
            return;
        }
        File file = new File(mContext.getCacheDir().getPath() + "/crash/" + mExceptionFileName);
        if (!file.exists()) {
            FileUtil.createFile(file.getPath());
        }
        writeExceptionToFile(info.getExceptionMsg(), file);
    }

    /**
     * 将异常日志写入文件
     */
    private void writeExceptionToFile(String message, File crashFile) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(CalendarUtils.getNowDataTime());
        stringBuffer.append("\n");
        stringBuffer.append(message);
        stringBuffer.append("\n");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(crashFile, true));
            writer.append(stringBuffer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Override
    public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {

    }

    @Override
    public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {

    }
}
