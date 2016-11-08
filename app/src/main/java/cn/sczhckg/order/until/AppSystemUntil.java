package cn.sczhckg.order.until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @ Describe:获取系统有关数据
 * Created by Like on 2016/11/3.
 * @ Email: 572919350@qq.com
 */

public class AppSystemUntil {

    /**
     * 判断网络是否链接
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取收设备唯一标识
     * @param mContext
     * @return
     */
    public static String getAndroidID(Context mContext){
        return Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
    }

}
