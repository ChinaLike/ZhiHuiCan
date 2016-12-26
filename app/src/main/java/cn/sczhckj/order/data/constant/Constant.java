package cn.sczhckj.order.data.constant;

/**
 * @describe: 请求字段常量
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface Constant {

    /**
     * 购物车数量加减时发布的事件标识
     */
    int CART_NUMBER_EVENT = 0;

    /**
     * 开桌
     */
    int OPEN_TABLE = 0;
    /**
     * 点菜
     */
    int ORDER = 1;
    /**
     * 结账
     */
    int SETTLE_ACCOUNTS = 2;

    /**
     * 用户登录信息传递
     */
    String USER_INFO = "userInfo";
    String INTENT_FLAG = "flag";

    /**
     * 首页锅底，推荐菜品等
     */
    String ID = "id";
    String DEVICE_ID = "deviceId";
    /**
     * 开桌数据请求
     */
    String TABLE = "table";
    String TYPE = "type";
    String PARAMS = "params";
    String PERSON = "person";

    /**
     * VIP申请
     */
    String APPLY_FOR_VIP_NAME = "name";
    String APPLY_FOR_VIP_PHONE = "phone";
    String APPLY_FOR_VIP_TYPE = "type";

    /**
     * 主菜单底部按钮和详情界面
     */
    int BOTTOM_ORDER = 0x501;
    int BOTTOM_SERVICE = 0x502;
    int BOTTOM_SETTLE_ACCOUNTS = 0x503;
    int DISHES_DETAILS_IN = 0x504;
    int DISHES_DETAILS_OUT = 0x505;

    /**
     * 支付方式
     */
    int CASH = 0;
    int WEIXIN = 1;
    int BANK_CART = 2;
    int ALIPAY = 3;

    /**
     * 界面间传递Flag
     */
    int LEAD_TO_LOGIN = 0x100;
    int MAIN_TO_LOGIN = 0x101;

    /**
     * 请求码，返回码requestCode, resultCode
     */
    int MAIN_REQUEST_CODE = 0x200;
    int LOGIN_RESULT_CODE = 0x201;
    /**
     * 点菜权限
     */
    int PERMISS_AGREE = 1;//可以操作点菜
    int PERMISS_DISAGREE = 0;//不可以操作点菜

    /**
     * 点菜类型 字段orderType
     */
    int ORDER_TYPE_OPEN = 0;//开桌点餐
    int ORDER_TYPE_ALONE = 1;//单桌点餐
    int ORDER_TYPE_MERGE = 2;//并卓点餐

    /**
     * 取价 字段active
     */
    int PRICE_DISACTIVE = 0;//不取此价
    int PRICE_ACTIVE = 1;//取此价

    /**
     * 菜品数量控制 字段maximum
     */
    int FOOD_DISASTRICT = 0;//不限制数量
    int FOOD_ASTRICT = 1;//限制数量，数量为给定大小，此处不一定为1

    /**
     * 必选菜品 字段required
     */
    int REQUIRED = 1;//必选
    int DIS_REQUIRED = 0;//可以不用必选

    /**
     * 接口回调台桌属性 字段tableType
     */
    int TABLE_TYPE_ALONE = 0;//单独点餐
    int TABLE_TYPE_MAIN = 1;//主桌点餐
    int TABLE_TYPE_AUX = 2;//辅桌点餐

}
