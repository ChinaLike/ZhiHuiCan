package cn.sczhckj.order.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import cn.sczhckj.order.data.bean.table.TableBean;

public abstract class BaseActivity extends FragmentActivity {

    /**
     * 锅底下标
     */
    protected final int FRAGMENT_REQUIRED = 0;
    /**
     * 菜品主页
     */
    protected final int FRAGMENT_MAIN = 1;
    /**
     * 详情界面
     */
    protected final int FRAGMENT_DETAILS = 2;
    /**
     * 办卡
     */
    protected final int FRAGMENT_CARD = 3;


    /**
     * 购物车界面
     */
    protected final int CART_DISHES = 0;
    /**
     * 结账清单
     */
    protected final int CART_BILL = 1;
    /**
     * 优惠列表
     */
    protected final int CART_FAVORABLE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void init();
}
