package cn.sczhckj.order;

import android.app.Application;

import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.manage.ExceptionPush;
import cn.sczhckj.order.mode.impl.StorageImpl;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class MyApplication extends Application {
    /**
     * 台桌初始化信息
     */
    public static TableBean tableBean = new TableBean();

    /**
     * 服务员模式下的设备ID,当为空时表示是消费者模式
     */
    public static String deviceID = "";
    /**
     * 平板模式0-消费者模式 1-服务员模式
     */
    public static int mode = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        /**异常信息收集*/
        ExceptionPush.init(this).openCrashHandler(Config.HOST, "p");

    }




}
