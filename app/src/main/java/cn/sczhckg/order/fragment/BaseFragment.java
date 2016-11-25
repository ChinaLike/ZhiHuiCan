package cn.sczhckg.order.fragment;

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

import java.util.ArrayList;
import java.util.List;

import cn.sczhckg.order.R;
import cn.sczhckg.order.adapter.DishesAdapter;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.listener.OnGiftListenner;
import cn.sczhckg.order.overwrite.DashlineItemDivider;
import cn.sczhckg.order.until.AppSystemUntil;
import cn.sczhckg.order.until.ConvertUtils;

/**
 * @describe:
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public abstract class BaseFragment extends Fragment {

    protected static DishesAdapter mDishesAdapter = null;

    protected List<DishesBean> parentDishesList = new ArrayList<>();
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
    protected static String deviceId ;

    protected View loadingView;

    private PopupWindow loadingPop;

    /**
     * pop的X轴坐标
     */
    private float X = 0;
    /**
     * pop的Y轴坐标
     */
    private float Y = 0;

    protected LinearLayout loading;

    protected LinearLayout loading_fail;

    private TextView loadingText;

    private TextView loadingFailText;

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
        if (mDishesAdapter == null) {
            mDishesAdapter = new DishesAdapter(getContext(), parentDishesList);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 100000, 1));
        mRecyclerView.setAdapter(mDishesAdapter);
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
            loadingPop.showAtLocation(loadingView, Gravity.NO_GRAVITY, (int)X, (int)Y);
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
            loadingPop.showAtLocation(loadingView, Gravity.NO_GRAVITY, (int)X, (int)Y);
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
    protected void loadingFail(String loadingFailText, View.OnClickListener onClickListener){
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
        initRect();
        loading = (LinearLayout) loadingView.findViewById(R.id.loading_parent);
        loading_fail = (LinearLayout) loadingView.findViewById(R.id.loading_fail);
        loadingText = (TextView) loadingView.findViewById(R.id.loading_title);
        loadingFailText = (TextView) loadingView.findViewById(R.id.loading_fail_title);
        loadingPop = new PopupWindow(loadingView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        loadingPop.setFocusable(true);
        loadingPop.setTouchable(true);
        loadingPop.setOutsideTouchable(true);
        loadingPop.setBackgroundDrawable(new ColorDrawable(0));
    }

    /**
     * 初始化Pop的位置
     */
    private void initRect() {
        int screenHeight = AppSystemUntil.height(getContext());
        int screenWidth = AppSystemUntil.width(getContext());
        Y = (screenHeight- ConvertUtils.dip2px(getContext(),getContext().getResources().getDimension(R.dimen.loading_progress))) / 2;
        X = screenWidth*18/26-ConvertUtils.dip2px(getContext(),getContext().getResources().getDimension(R.dimen.loading_progress))/2;
    }

}
