package cn.sczhckj.order.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.CloseServiceEvent;
import cn.sczhckj.order.data.listener.OnTipListenner;
import cn.sczhckj.order.service.QRCodeService;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public abstract class BaseFragment extends Fragment {

    /**
     * 打赏接口
     */
    protected static OnTipListenner onTipListenner;

    /**
     * 点餐方式，默认单桌点餐
     */
    public static int orderType = Constant.ORDER_TYPE_ALONE;

    /**
     * 界面标识  0-锅底必选  1-点菜界面
     */
    public static int flag = 0;
    /**
     * 设备ID
     */
    public static String deviceId;

    protected View loadingView;

    private PopupWindow loadingPop;

    protected LinearLayout loading;

    protected LinearLayout loading_fail;

    private TextView loadingText;

    private TextView loadingFailText;
    /**
     * 购物车刷新
     */
    private Intent mCartIntent;
    /**
     * 二维码扫描
     */
    private Intent mQRCodeIntent;

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
    protected static List<FoodBean> orderList = new ArrayList<>();
    /**
     * 未下单的菜品，即购物车菜品
     */
    protected static List<FoodBean> disOrderList = new ArrayList<>();
    /**
     * 是否加菜
     */
    public static boolean isAddFood = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    public abstract void setData(Object object);

    public abstract void init();

    public void setOnGiftListenner(OnTipListenner onTipListenner) {
        this.onTipListenner = onTipListenner;
    }

    public int getFlag() {
        return flag;
    }

    /**
     * 显示进度加载框
     */
    protected void showProgress() {
        if (!loadingPop.isShowing()) {
            Log.d("PopWindow", "show");
            loading.setVisibility(View.VISIBLE);
            loading_fail.setVisibility(View.GONE);
            loadingPop.showAtLocation(loadingView, Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 显示进度加载框
     */
    protected void showProgress(String loadingText) {
        if (!loadingPop.isShowing()) {
            Log.d("PopWindow", "show");
            loading.setVisibility(View.VISIBLE);
            loading_fail.setVisibility(View.GONE);
            loadingPop.showAtLocation(loadingView, Gravity.CENTER, 0, 0);
            this.loadingText.setText(loadingText);
        }
    }

    /**
     * 隐藏进度加载框
     */
    protected void dismissProgress() {
        Log.d("PopWindow", "dismiss");
        loadingPop.dismiss();
    }

    /**
     * 加载失败
     */
    protected void loadingFail(String loadingFailText, View.OnClickListener onClickListener) {
        loading.setVisibility(View.GONE);
        loading_fail.setVisibility(View.VISIBLE);
        this.loadingFailText.setText(loadingFailText);
        loading_fail.setOnClickListener(onClickListener);
    }

    /**
     * 初始化进度加载框
     */
    protected void initLoadingPop() {
        Log.d("PopWindow", "init");
        loadingView = LayoutInflater.from(getContext()).inflate(R.layout.item_loading, null, false);
        loading = (LinearLayout) loadingView.findViewById(R.id.loading_parent);
        loading_fail = (LinearLayout) loadingView.findViewById(R.id.loading_fail);
        loadingText = (TextView) loadingView.findViewById(R.id.loading_title);
        loadingFailText = (TextView) loadingView.findViewById(R.id.loading_fail_title);
        loadingPop = new PopupWindow(loadingView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        loadingPop.setFocusable(true);
        loadingPop.setTouchable(true);
        loadingPop.setOutsideTouchable(false);
        loadingPop.setBackgroundDrawable(new ColorDrawable(0));
    }

    /**
     * 完成后关闭界面，推到会员登录界面
     */
    protected void finish() {
        getActivity().finish();
        /**设置为未登录模式*/
        MyApplication.isLogin = false;
        /**销毁时关闭服务*/
        EventBus.getDefault().post(new CloseServiceEvent());
        /**人数清零*/
        MainActivity.personNumber = 0;
        /**设置默认点餐为单桌点餐*/
        orderType = Constant.ORDER_TYPE_ALONE;
        /**清空用户编码记录*/
        MyApplication.memberCode = null;
        /**设置菜品过多界线*/
        warmPromptNumber = 0;
        /**是否开桌*/
        isOpen = false;
        /**消费记录ID*/
        MyApplication.recordId = null;
        /**清空购物车数据*/
        orderList = new ArrayList<>();
        disOrderList = new ArrayList<>();
        /**设置默认是否加菜*/
        isAddFood = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    /**
     * 开启扫码服务
     */
    protected void startQRCodeService() {
        mQRCodeIntent = new Intent(getActivity(), QRCodeService.class);
        getActivity().startService(mQRCodeIntent);
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
    protected void loading(LinearLayout loadingParent, LinearLayout contextParent, LinearLayout loadingItemParent, LinearLayout loadingFail, TextView loadingTitle, String str) {
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
    protected void loadingSuccess(LinearLayout loadingParent, LinearLayout contextParent, LinearLayout loadingItemParent, LinearLayout loadingFail) {
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
    protected void loadingFail(LinearLayout loadingParent, LinearLayout contextParent, LinearLayout loadingItemParent, LinearLayout loadingFail, TextView loadingFailTitle, String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.GONE);
        loadingFail.setVisibility(View.VISIBLE);
        loadingFailTitle.setText(str);
    }

}
