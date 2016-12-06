package cn.sczhckj.order.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {

    /**
     * 锅底下标
     */
    protected final int FRAGMENT_POT_TYPE = 0;
    /**
     * 菜品主页
     */
    protected final int FRAGMENT_MAIN = 1;
    /**
     * 团购券
     */
    protected final int FRAGMENT_GROUPON = 2;
    /**
     * 打赏
     */
    protected final int FRAGMENT_GIFT = 3;
    /**
     * 支付完成界面或扫码界面
     */
    protected final int FRAGMENT_QRCODE = 4;
    /**
     * 评价界面
     */
    protected final int FRAGMENT_EVALUATE = 5;
    /**
     * 详情界面
     */
    protected final int FRAGMENT_DETAILS = 6;
    /**
     * 申请办理VIP界面
     */
    protected final int APPLY_FOR_VIP_CARD = 7;
    /**
     * 购物车商品界面
     */
    protected final int CART_DISHES = 0;
    /**
     * 结账
     */
    protected final int CART_SETTLT_AOUNTS = 1;
    /**
     * 评价列表
     */
    protected final int CART_EVALUATE_LIST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void init();

    protected abstract void initNetData();

}
