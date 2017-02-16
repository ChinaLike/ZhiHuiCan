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
     * IP
     */
    public static final String IP = "192.168.0.50";

    /**
     * 主机
     */
    public static String HOST = "http://" + IP + ":8080/" + ADDRESS;
    /**
     * 心跳检测
     */
    public static String URL_HEART_SERVICE = "ws://" + IP + ":8080/" + ADDRESS + "ws/heart?username=";
    /**
     * 指令推送
     */
    public static String URL_NOTIFICATION_SERVICE = "ws://" + IP + ":8080/" + ADDRESS + "ws/notification?username=";

}
