package cn.sczhckj.order;

/**
 * @describe: 接口配置
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class Config {

    public static final String IP="192.168.0.50";

    /**
     * 主机
     */
    public static String HOST = "http://"+IP+":8080/pad/";

//    /**
//     * 菜品完成进度推送
//     */
//    public static String URL_FOOD_SERVICE = "ws://"+IP+":8080/pad/ws/food?username=";
//
//    /**
//     * 服务终止推送
//     */
//    public static String URL_SERVICE_SERVICE = "ws://"+IP+":8080/pad/ws/service?username=";
//
//    /**
//     * 解锁、锁定界面推送
//     */
//    public static String URL_LOCK_SERVICE = "ws://"+IP+":8080/pad/ws/lock?username=";

    /**
     * 心跳检测
     */
    public static String URL_HEART_SERVICE = "ws://"+IP+":8080/pad/ws/heart?username=";

//    /**
//     * 数据刷新
//     */
//    public static String URL_REFRESH_SERVICE = "ws://"+IP+":8080/pad/ws/refresh?username=";
    /**
     * 指令推送
     */
    public static String URL_NOTIFICATION_SERVICE = "ws://"+IP+":8080/pad/ws/notification?username=";

}
