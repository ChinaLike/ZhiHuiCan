package cn.sczhckj.order.data.bean;

/**
 * @describe: 请求字段常量
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class Constant {

    /**
     * 购物车数量加减时发布的事件标识
     */
    public static final int CART_NUMBER_EVENT = 0;

    /**
     * 开桌
     */
    public final static int OPEN_TABLE = 0;
    /**
     * 点菜
     */
    public final static int ORDER = 1;
    /**
     * 结账
     */
    public final static int SETTLE_ACCOUNTS = 2;

    /**
     * 用户登录信息传递
     */
    public final static String USER_INFO = "userInfo";
    public final static String INTENT_FLAG = "flag";

    /**
     * 首页锅底，推荐菜品等
     */
    public final static String ID = "id";
    public final static String DEVICE_ID = "deviceId";
    /**
     * 开桌数据请求
     */
    public final static String TABLE = "table";
    public final static String TYPE = "type";
    public final static String PARAMS = "params";
    public final static String PERSON = "person";

    /**
     * VIP申请
     */
    public final static String APPLY_FOR_VIP_NAME = "name";
    public final static String APPLY_FOR_VIP_PHONE = "phone";
    public final static String APPLY_FOR_VIP_TYPE = "type";

    /**
     * 主菜单底部按钮和详情界面
     */
    public final static int BOTTOM_ORDER = 0x501;
    public final static int BOTTOM_SERVICE = 0x502;
    public final static int BOTTOM_SETTLE_ACCOUNTS = 0x503;
    public final static int DISHES_DETAILS_IN = 0x504;
    public final static int DISHES_DETAILS_OUT = 0x505;

    /**
     * 支付方式
     */
    public final static int CASH = 0;
    public final static int WEIXIN = 1;
    public final static int BANK_CART = 2;
    public final static int ALIPAY = 3;

    /**
     * 界面间传递Flag
     */
    public final static int LEAD_TO_LOGIN = 0x100;
    public final static int MAIN_TO_LOGIN = 0x101;

    /**
     * 请求码，返回码requestCode, resultCode
     */
    public final static int MAIN_REQUEST_CODE = 0x200;
    public final static int LOGIN_RESULT_CODE = 0x201;
    /**
     * 点菜权限
     */
    public final static int PREMISS_AGREE = 1;//可以操作点菜
    public final static int PREMISS_DISAGREE = 0;//不可以操作点菜

    /**
     * 点菜类型 字段orderType
     */
    public final static int ORDER_TYPE_OPEN = 0;//开桌点餐
    public final static int ORDER_TYPE_ALONE = 1;//单桌点餐
    public final static int ORDER_TYPE_MERGE = 2;//并卓点餐

    /**取价 字段active*/
    public final static int PRICE_DISACTIVE=0;//不取此价
    public final static int PRICE_ACTIVE=1;//取此价

    /**菜品数量控制 字段maximum*/
    public final static int FOOD_DISASTRICT=0;//不限制数量
    public final static int FOOD_ASTRICT=1;//限制数量，数量为给定大小，此处不一定为1

    /**必选菜品 字段required*/
    public final static int REQUIRED=1;//必选
    public final static int DIS_REQUIRED=0;//可以不用必选

}
