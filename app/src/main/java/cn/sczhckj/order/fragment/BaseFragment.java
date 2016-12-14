package cn.sczhckj.order.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.sczhckj.order.adapter.FoodAdapter;
import cn.sczhckj.order.data.bean.FoodBean;
import cn.sczhckj.order.data.event.CloseServiceEvent;
import cn.sczhckj.order.data.listener.OnGiftListenner;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.service.CartService;
import cn.sczhckj.order.service.QRCodeService;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public abstract class BaseFragment extends Fragment {

    protected FoodAdapter mFoodAdapter = null;

    protected List<FoodBean> parentDishesList = new ArrayList<>();
    /**
     * 打赏接口
     */
    protected static OnGiftListenner onGiftListenner;

    /**
     * 点餐类型 0-单桌点餐  1-并桌点餐
     */
    public static final int ALONE_ORDER = 0;
    public static final int MERGER_ORDER = 1;

    protected int orderType = ALONE_ORDER;

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
     * 锅底数量
     */
    public static int totalPotNumber = 0;

    protected static String openTablePassword="";

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
    }

    public abstract void setData(Object object);

    public abstract void init();

    /**
     * 菜品适配
     *
     * @param mRecyclerView
     */
    protected void initDishesAdapter(RecyclerView mRecyclerView) {
        mFoodAdapter = new FoodAdapter(getContext(), parentDishesList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mFoodAdapter);
        mRecyclerView.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
    }

    public void setOnGiftListenner(OnGiftListenner onGiftListenner) {
        this.onGiftListenner = onGiftListenner;
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
        MyApplication.isLogin = false;
        /**销毁时关闭服务*/
        EventBus.getDefault().post(new CloseServiceEvent());
        /**把锅底数量职位初始值*/
        totalPotNumber = 0;
        /**初始密码*/
        openTablePassword="";
        /**人数清零*/
        MainActivity.personNumber=0;
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
     * 开启购物车刷新服务
     */
    protected void startCartService() {
        mCartIntent = new Intent(getActivity(), CartService.class);
        getActivity().startService(mCartIntent);
    }

}
