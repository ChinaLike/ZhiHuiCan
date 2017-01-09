package cn.sczhckj.order;

import android.app.Application;

import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.constant.FileConstant;
import cn.sczhckj.order.mode.impl.StorageImpl;
import cn.sczhckj.order.mode.impl.WebSocketImpl;
import cn.sczhckj.order.until.show.L;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class MyApplication extends Application {
    /**
     * 用户是否登录
     */
    public static boolean isLogin = false;
    /**
     * 用户编码，即手机号或卡号
     */
    public static String memberCode;
    /**
     * 消费记录ID
     */
    public static Integer recordId;
    /**
     * 数据存储
     */
    public static StorageImpl mStorage;
    /**
     * 台桌状态
     */
    public static int status = Constant.TABLE_STATUS_EMPTY;
    /**
     * 菜品过多提醒
     */
    public static int foodCountHint;

    @Override
    public void onCreate() {
        super.onCreate();

        mStorage = new StorageImpl(getApplicationContext(), FileConstant.USER);

        initStorage();

    }

    /**
     * 从ShareP中取出数据
     */
    private void initStorage() {

        isLogin = (boolean) mStorage.getData(Constant.STORAGR_IS_LOGIN, false);
        memberCode = (String) mStorage.getData(Constant.STORAGR_MEMBER_CODE, "");
        recordId = (Integer) mStorage.getData(Constant.STORAGR_RECORDID, -1);
    }

    public static void setIsLogin(boolean isLogin) {
        MyApplication.isLogin = isLogin;
        mStorage.setData(Constant.STORAGR_IS_LOGIN, isLogin);
    }

    public static void setMemberCode(String memberCode) {
        MyApplication.memberCode = memberCode;
        mStorage.setData(Constant.STORAGR_MEMBER_CODE, memberCode);
    }

    public static void setRecordId(Integer recordId) {
        MyApplication.recordId = recordId;
        mStorage.setData(Constant.STORAGR_RECORDID, recordId);
    }

    public static void setFoodCountHint(int foodCountHint) {
        MyApplication.foodCountHint = foodCountHint;
        mStorage.setData(Constant.STORAGR_HINT, foodCountHint);
    }

    public static void setStatus(int status) {
        MyApplication.status = status;
    }
}
