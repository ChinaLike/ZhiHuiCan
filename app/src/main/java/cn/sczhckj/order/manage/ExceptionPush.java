package cn.sczhckj.order.manage;

import android.content.Context;

/**
 * @ describe: 异常信息提交
 * @ author: Like on 2017-02-16.
 * @ email: 572919350@qq.com
 */

public class ExceptionPush {

    private static final String TAG = "AbsFrame";
    private static final Object LOCK = new Object();
    private volatile static ExceptionPush mManager = null;
    private Context mContext;

    private ExceptionPush() {

    }

    private ExceptionPush(Context context) {
        mContext = context;
    }

    /**
     * 初始化框架
     *
     * @param applicationContext
     * @return
     */
    public static ExceptionPush init(Context applicationContext) {
        if (mManager == null) {
            synchronized (LOCK) {
                if (mManager == null) {
                    mManager = new ExceptionPush(applicationContext);
                }
            }
        }
        return mManager;
    }

    /**
     * 获取AppManager管流程实例
     *
     * @return
     */
    public static ExceptionPush getInstance() {
        if (mManager == null) {
            throw new NullPointerException("请在application 的 onCreate 方法里面使用MVVMFrame.init()方法进行初始化操作");
        }
        return mManager;
    }

    /**
     * 开启异常捕获
     * 日志文件位于/data/data/Package Name/cache//crash/AbsExceptionFile.crash
     */
    public void openCrashHandler() {
        openCrashHandler("", "");
    }

    /**
     * 开启异常捕获
     * 需要网络权限，get请求，异常参数，需要下面两个网络权限
     * android:name="android.permission.INTERNET"
     * android:name="android.permission.ACCESS_NETWORK_STATE"
     *
     * @param serverHost 服务器地址
     * @param key        数据传输键值
     */
    public void openCrashHandler(String serverHost, String key) {
        CrashHandler handler = CrashHandler.getInstance(mContext);
        handler.setServerHost(serverHost, key);
        Thread.setDefaultUncaughtExceptionHandler(handler);
    }

}
