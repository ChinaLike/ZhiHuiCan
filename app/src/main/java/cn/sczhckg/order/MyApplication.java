package cn.sczhckg.order;

import android.app.Application;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class MyApplication extends Application{

    public static boolean isLogin=false;

    public static String userName;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
