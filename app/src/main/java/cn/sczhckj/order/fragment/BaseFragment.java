package cn.sczhckj.order.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.until.show.L;
import pl.droidsonroids.gif.GifImageView;

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
    /**
     * 加载中父类
     */
    private LinearLayout loadingParent;
    /**
     * 加载动画
     */
    private GifImageView mGifImageView;
    /**
     * 提示语
     */
    private TextView mTextView;

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

    protected View view;

    @Nullable
    @Bind(R.id.parent)
    View parent;
    @Nullable
    @Bind(R.id.temp_parent)
    LinearLayout tempParent;
    @Nullable
    @Bind(R.id.temp_gif)
    GifImageView tempGif;
    @Nullable
    @Bind(R.id.temp_text)
    TextView tempText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setLayoutId(), null, false);
        ButterKnife.bind(this, view);
        initLoadingPop();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (cateList == null) {
            cateList = new ArrayList<>();
        }
        setIsOpen();
    }

    /**
     * 布局文件
     *
     * @return
     */
    public abstract int setLayoutId();

    /**
     * 设置数据
     *
     * @param object
     */
    public abstract void setData(Object object);

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 加载失败
     */
    public abstract void initFail();

    /**
     * 加载失败
     */
    public abstract void loadingFail();

    /**
     * 设置是否开桌
     */
    private void setIsOpen() {
        int status = MyApplication.tableBean.getStatus();
        if (status == Constant.TABLE_STATUS_OPEN || status == Constant.TABLE_STATUS_FOOD
                || status == Constant.TABLE_STATUS_BILL || status == Constant.TABLE_STATUS_BILL_MERGE) {
            isOpen = true;
        }
    }

    /**
     * 显示进度加载框
     */
    protected void showProgress(String loadingText) {
        loadingParent.setClickable(false);
        mTextView.setText(loadingText);
        mGifImageView.setImageResource(R.drawable.loading);
        mPopupWindow.showAtLocation(popLoadingView, Gravity.CENTER, 0, 0);

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
    protected void loadingFail(String loadingFailText) {
        loadingParent.setClickable(true);
        mTextView.setText(loadingFailText);
        loadingParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingFail();
            }
        });
    }

    /**
     * 初始化数据提交Pop数据
     */
    protected void initLoadingPop() {
        popLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.pop_loading, null, false);
        loadingParent = (LinearLayout) popLoadingView.findViewById(R.id.loading_parent);
        mTextView = (TextView) popLoadingView.findViewById(R.id.loading_text);
        mGifImageView = (GifImageView) popLoadingView.findViewById(R.id.loading_gif);
        mPopupWindow = new PopupWindow(popLoadingView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE && !mPopupWindow.isFocusable()) {
                    //如果焦点不在popupWindow上，且点击了外面，不再往下dispatch事件：
                    //不做任何响应,不 dismiss popupWindow
                    return true;
                }
                //否则default，往下dispatch事件:关掉popupWindow，
                return false;
            }
        });
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
     * 初始化中
     *
     * @param text 初始化提示文本
     */
    protected void initing(String text) {
        parent.setVisibility(View.GONE);
        tempParent.setVisibility(View.VISIBLE);
        tempText.setText(text);
        tempGif.setImageResource(R.drawable.init_loading);
    }

    /**
     * 加载成功
     */
    protected void initSuccess() {
        parent.setVisibility(View.VISIBLE);
        tempParent.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    protected void initFailer(String text) {
        tempText.setText(text);
        tempGif.setImageResource(R.drawable.init_loading_faild);
        tempParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFail();
            }
        });
    }

    /**
     * 是否消费中（包括已开桌，上菜中，结账中）
     *
     * @return
     */
    protected boolean isConsuming() {
        Integer status = MyApplication.tableBean.getStatus();
        if (status == Constant.TABLE_STATUS_OPEN || status == Constant.TABLE_STATUS_FOOD
                || status == Constant.TABLE_STATUS_BILL || status == Constant.TABLE_STATUS_BILL_MERGE) {
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
