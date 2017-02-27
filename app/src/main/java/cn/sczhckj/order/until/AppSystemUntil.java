package cn.sczhckj.order.until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.WindowManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.until.show.L;

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
     * 获取收设备唯一标识(判断服务员模式)
     *
     * @param mContext
     * @return
     */
    public static String getAndroidID(Context mContext) {
        String deviceID = Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
        if (MyApplication.deviceID == null || MyApplication.deviceID.equals("")) {
            return deviceID;
        } else {
            return MyApplication.deviceID;
        }
    }

    /**
     * 获取设备ID（原始）
     * @param mContext
     * @return
     */
    public static String getHeartAndroidID(Context mContext) {
        return Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
    }

    /**
     * 屏幕的宽度
     *
     * @param mContext
     * @return
     */
    public static int width(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 屏幕的高度
     *
     * @param mContext
     * @return
     */
    public static int height(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取IP
     *
     * @param mContext
     * @return
     */
    public static String ip(Context mContext) {
        NetworkInfo info = ((ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
            return ipAddress;
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return  (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

}
