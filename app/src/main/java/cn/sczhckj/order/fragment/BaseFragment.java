package cn.sczhckj.order.fragment;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.BaseActivity;
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.until.show.L;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 点餐方式，默认单桌点餐
     */
    public static int orderType = Constant.ORDER_TYPE_ALONE;

    protected View popLoadingView;

    private PopupWindow mPopupWindow;

    protected LinearLayout popLoading;

    protected LinearLayout popLoadingFail;

    private TextView popLoadingText;

    private TextView popLoadingFailText;

    public Context mContext;

    /**
     * 分类集合
     */
    protected static List<CateBean.CateItemBean> cateList;

    /**
     * 菜品过多温馨提示界线
     */
    public static int warmPromptNumber = 0;
    /**
     * 是否开桌
     */
    public static boolean isOpen = false;

    /**
     * 下单的菜品，即已下单菜品
     */
    public static List<FoodBean> orderList = new ArrayList<>();
    /**
     * 未下单的菜品，即购物车菜品
     */
    public static List<FoodBean> disOrderList = new ArrayList<>();
    /**
     * 是否加菜
     */
    public static boolean isAddFood = false;
    /**
     * 已点赞菜品
     */
    public static List<FoodBean> favorFood = new ArrayList<>();
    /**
     * 当前台桌ID
     */
    protected static Integer tableId = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (cateList == null) {
            cateList = new ArrayList<>();
        }
        setIsOpen();
    }

    public abstract void setData(Object object);

    public abstract void init();

    /**
     * 设置是否开桌
     */
    private void setIsOpen() {
        int status = MyApplication.tableBean.getStatus();
        if (status == Constant.TABLE_STATUS_OPEN || status == Constant.TABLE_STATUS_FOOD
                || status == Constant.TABLE_STATUS_BILL) {
            isOpen = true;
        }
    }

    /**
     * 显示进度加载框
     */
    protected void showProgress() {
        if (!mPopupWindow.isShowing()) {
            popLoading.setVisibility(View.VISIBLE);
            popLoadingFail.setVisibility(View.GONE);
            mPopupWindow.showAtLocation(popLoadingView, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 显示进度加载框
     */
    protected void showProgress(String loadingText) {
        if (!mPopupWindow.isShowing()) {
            popLoading.setVisibility(View.VISIBLE);
            popLoadingFail.setVisibility(View.GONE);
            mPopupWindow.showAtLocation(popLoadingView, Gravity.CENTER, 0, 0);
            this.popLoadingText.setText(loadingText);
        }
    }

    /**
     * 隐藏进度加载框
     */
    protected void dismissProgress() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 加载失败
     */
    protected void loadingFail(String loadingFailText, View.OnClickListener onClickListener) {
        popLoading.setVisibility(View.GONE);
        popLoadingFail.setVisibility(View.VISIBLE);
        this.popLoadingFailText.setText(loadingFailText);
        popLoadingFail.setOnClickListener(onClickListener);
    }

    /**
     * 初始化进度加载框
     */
    protected void initLoadingPop() {
        popLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.item_pop_loading, null, false);
        popLoading = (LinearLayout) popLoadingView.findViewById(R.id.loading_parent);
        popLoadingFail = (LinearLayout) popLoadingView.findViewById(R.id.loading_fail);
        popLoadingText = (TextView) popLoadingView.findViewById(R.id.loading_title);
        popLoadingFailText = (TextView) popLoadingView.findViewById(R.id.loading_fail_title);
        mPopupWindow = new PopupWindow(popLoadingView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
    }

    /**
     * 完成后关闭界面，推到会员登录界面
     */
    protected void finish() {
        MyApplication.tableBean = new TableBean();
        setDefaultTable();
        /**人数清零*/
        MainActivity.personNumber = 0;
        /**设置默认点餐为单桌点餐*/
        orderType = Constant.ORDER_TYPE_ALONE;
        /**设置菜品过多界线*/
        warmPromptNumber = 0;
        /**是否开桌*/
        isOpen = false;
        /**清空购物车数据*/
        orderList = new ArrayList<>();
        disOrderList = new ArrayList<>();
        /**设置默认是否加菜*/
        isAddFood = false;
        /**清除已点赞菜品*/
        favorFood = new ArrayList<>();
        /**清除台桌ID*/
        tableId = -1;
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void setDefaultTable() {
        /**点餐类型默认为单独点餐*/
        MyApplication.tableBean.setOrderType(Constant.ORDER_TYPE_ALONE);
        /**设置默认菜品提醒数量，默认无上限*/
        MyApplication.tableBean.setFoodCountHint(10000);
        /**设置点菜方式显示*/
        MyApplication.tableBean.setIsShow(Constant.DIS_SHOW_TYPE);
        /**设置台桌状态为空*/
        MyApplication.tableBean.setStatus(Constant.TABLE_STATUS_EMPTY);
        /**设置为未登录*/
        MyApplication.tableBean.setLogin(false);
    }

    /**
     * 加载中
     *
     * @param loadingParent     显示加载区域控件
     * @param contextParent     显示内容区域控件
     * @param loadingItemParent 加载区域父类
     * @param loadingFail       加载失败控件
     * @param loadingTitle      加载成功提示语
     * @param str
     */
    protected void loading(LinearLayout loadingParent, View contextParent, LinearLayout loadingItemParent, LinearLayout loadingFail, TextView loadingTitle, String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
        loadingTitle.setText(str);
    }

    /**
     * 加载成功
     *
     * @param loadingParent     显示加载区域控件
     * @param contextParent     显示内容区域控件
     * @param loadingItemParent 加载区域父类
     * @param loadingFail       加载失败控件
     */
    protected void loadingSuccess(LinearLayout loadingParent, View contextParent, LinearLayout loadingItemParent, LinearLayout loadingFail) {
        loadingParent.setVisibility(View.GONE);
        contextParent.setVisibility(View.VISIBLE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     *
     * @param loadingParent     显示加载区域控件
     * @param contextParent     显示内容区域控件
     * @param loadingItemParent 加载区域父类
     * @param loadingFail       加载失败控件
     * @param loadingFailTitle  加载失败提示语
     * @param str
     */
    protected void loadingFail(LinearLayout loadingParent, View contextParent, LinearLayout loadingItemParent, LinearLayout loadingFail, TextView loadingFailTitle, String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.GONE);
        loadingFail.setVisibility(View.VISIBLE);
        loadingFailTitle.setText(str);
    }

    /**
     * 是否消费中（包括已开桌，上菜中，结账中）
     *
     * @return
     */
    protected boolean isConsuming() {
        Integer status = MyApplication.tableBean.getStatus();
        if (status == Constant.TABLE_STATUS_OPEN || status == Constant.TABLE_STATUS_FOOD
                || status == Constant.TABLE_STATUS_BILL) {
            warmPromptNumber = MyApplication.tableBean.getFoodCountHint();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 存储点菜方式
     *
     * @param orderType
     */
    public static void setOrderType(int orderType) {
        BaseFragment.orderType = orderType;
        MyApplication.tableBean.setOrderType(orderType);
    }

    /**
     * 获取点菜方式
     *
     * @return
     */
    public static int getOrderType() {
        if (MyApplication.tableBean == null) {
            return Constant.ORDER_TYPE_ALONE;
        }
        return MyApplication.tableBean.getOrderType();
    }

}
