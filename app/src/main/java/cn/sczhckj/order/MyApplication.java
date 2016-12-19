package cn.sczhckj.order;

import android.app.Application;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class MyApplication extends Application {
    /**
     * 用户是否登录
     */
    public static boolean isLogin = false;
    /**
     * 用户编码，即手机号或卡号
     */
    public static String memberCode;
    /**
     * 消费记录ID
     */
    public static Integer recordId;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
