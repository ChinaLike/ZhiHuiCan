package cn.sczhckj.order;

/**
 * @describe: 接口配置
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class Config {

    /**
     * 地址
     */
//    private static final String ADDRESS = "cn.sczhckj.pad.rest/";
    private static final String ADDRESS = "pad/";

    /**
     * 主机
     */
    public static String HOST = "http://" + host() + ADDRESS;
    /**
     * 心跳检测
     */
    public static String URL_HEART_SERVICE = "ws://" + host() + ADDRESS + "ws/heart?username=";
    /**
     * 指令推送
     */
    public static String URL_NOTIFICATION_SERVICE = "ws://" + host() + ADDRESS + "ws/notification?username=";

    /**
     * 获取IP
     *
     * @return
     */
    public static String ip() {
        return (String) MyApplication.share.getData("ip", "192.168.0.25");
    }

    public static String port() {
        return ":" + MyApplication.share.getData("port", "8080") + "/";
    }

    public static String host() {
        return ip() + port();
    }

    public static void setHOST() {
        HOST = "http://" + host() + ADDRESS;
    }

    public static void setUrlHeartService() {
        URL_HEART_SERVICE = "ws://" + host() + ADDRESS + "ws/heart?username=";
    }

    public static void setUrlNotificationService() {
        URL_NOTIFICATION_SERVICE = "ws://" + host() + ADDRESS + "ws/notification?username=";
    }
}
