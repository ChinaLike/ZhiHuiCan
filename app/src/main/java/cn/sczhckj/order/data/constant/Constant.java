package cn.sczhckj.order.data.constant;

/**
 * @describe: 请求字段常量
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public interface Constant {

    /**
     * 底部按钮
     */
    int OPEN_TABLE = 0;//开桌
    int ORDER = 1;//点菜
    int SETTLE_ACCOUNTS = 2;//结账

    /**
     * 用户登录信息传递
     */
    String USER_INFO = "userInfo";
    String INTENT_FLAG = "flag";

    /**
     * Intent传递字段
     */
    String LOCK_TITLE = "lock";
    String INTENT_TABLE_STATUS = "status";
    String INTENT_TABLE_REMARK = "remark";

    /**
     * 存储字段
     */
    String STORAGR_IS_LOGIN = "isLogin";//是否登录
    String STORAGR_MEMBER_CODE = "memberCode";//用户编码
    String STORAGR_RECORDID = "recordId";//消费记录ID
    String STORAGR_PERSON = "person";//就餐人数
    String STORAGR_HINT = "hint";//菜品过多提醒
    String STORAGR_SHOW_TYPE = "isShow";//是否显示点菜方式
    String STORAGR_ORDER_TYPE = "orderType";//已选择点菜方式

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
    int ORDER_TYPE_MERGE = 2;//并桌点餐

    /**
     * 台桌是否合并
     */
    int MERGE_TABLE = 1;//合并台桌
    int NON_MERGE_TABLE = 0;//不合并台桌

    /**
     * 消费类型
     */
    int CONSUME_TYPE_ALONE = 0;//单独消费
    int CONSUME_TYPE_MERGE_MAIN = 1;//并桌消费(主桌)
    int CONSUME_TYPE_MERGE_ASSIST = 2;//并桌消费(辅桌)

    /**
     * 是否显示点菜类型 字段showType
     */
    int DIS_SHOW_TYPE = 0;//不显示
    int SHOW_TYPE = 1;//显示

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
    int TABLE_TYPE_ALONE = 0;//单桌
    int TABLE_TYPE_MAIN = 1;//并桌主桌
    int TABLE_TYPE_AUX = 2;//并桌辅桌

    /**
     * 当前VIP是否解锁 字段isLock
     */
    int UNLOCK = 1;//解锁
    int LOCK = 0;//未解锁

    /**
     * 操作类型ID
     */
    int OPERATE_ID = 0;//0-普通退菜（平板）1-特权退菜（收银）

    /**
     * 台桌状态
     */
    int TABLE_STATUS_DISABLE = 1;//不可用
    int TABLE_STATUS_NO_OPEN = 2;//未开台
    int TABLE_STATUS_RESERVE = 3;//预定
    int TABLE_STATUS_EMPTY = 4;//空桌
    int TABLE_STATUS_OPEN = 5;//已开桌
    int TABLE_STATUS_FOOD = 6;//已上菜
    int TABLE_STATUS_BILL = 7;//结帐中
    int TABLE_STATUS_SWEEP = 8;//打扫中
    int TABLE_STATUS_OTHER = -1;//其他

    /**
     * 版本类型0-点菜端 1-后厨端
     */
    int VERSION_TYPE = 0;

    /**
     * 平板模式
     */
    int CUSTOMER = 0;//消费者模式
    int PRODUCER = 1;//服务员模式


}
