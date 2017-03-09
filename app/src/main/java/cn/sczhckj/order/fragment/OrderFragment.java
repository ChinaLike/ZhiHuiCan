package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.CatesAdapter;
import cn.sczhckj.order.adapter.FoodAdapter;
import cn.sczhckj.order.adapter.TabTableAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.table.InfoBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.MoreDishesHintEvent;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.FoodRefreshImpl;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import cn.sczhckj.order.until.show.L;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 点餐界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class OrderFragment extends BaseFragment implements CatesAdapter.OnItemClickListener, OnItemClickListener {


    @Bind(R.id.dishes_tab)
    RecyclerView dishesTab;
    @Bind(R.id.dishes_classify)
    RecyclerView dishesClassify;
    @Bind(R.id.dishes_show)
    RecyclerView dishesShow;
    @Bind(R.id.tab_text)
    TextView tabText;

    /**
     * 一级分类分类适配器
     */
    private CatesAdapter mCatesAdapter;
    /**
     * 导航栏适配器
     */
    private TabTableAdapter mTabTableAdapter;
    /**
     * 初始化菜品适配器
     */
    private FoodAdapter mFoodAdapter;
    /**
     * 菜品分类数据请求
     */
    private FoodMode mFoodMode;
    /**
     * 台桌分类数据请求
     */
    private TableMode mTableMode;
    /**
     * 默认显示第几个
     */
    private int defaultItem = 0;

    private LinearLayoutManager mLinearLayoutManager;
    /**
     * 临时菜品列表
     */
    private List<FoodBean> foodList = new ArrayList<>();

    /**
     * 是否显示提示
     */
    private boolean isHint = true;

    private PopupWindow mPopupWindow = null;
    /**
     * Pop视图
     */
    private View popView;
    /**
     * Pop的宽高
     */
    private static final float POP_WIDTH = 400;
    private static final float POP_HEIGHT = 230;

    /**
     * 当前点击的Item下标
     */
    private int currPosition = -1;
    /**
     * 当前点击的Item参数
     */
    private Object currBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**注册事件监听*/
        EventBus.getDefault().register(this);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPop();

        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void init() {
        mFoodMode = new FoodMode();
        mTableMode = new TableMode();
        initCateAdapter();
        initTabAdapter();
        initFoodAdapter();
        initCate();
        initTab();
    }

    @Override
    public void initFail() {
        /**加载失败时使用*/
        initCate();
        initTab();
    }

    @Override
    public void loadingFail() {

    }

    @Override
    public void setData(Object object) {

    }

    /**
     * 初始化分类适配器
     */
    private void initCateAdapter() {
        mCatesAdapter = new CatesAdapter(mContext, null, defaultItem);
        /**添加Item点击监听*/
        mCatesAdapter.addOnItemClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        dishesClassify.setLayoutManager(mLinearLayoutManager);
        dishesClassify.setAdapter(mCatesAdapter);
        dishesClassify.addItemDecoration(
                new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.line_s), 5, 1));
    }

    /**
     * 初始化头部Tab适配器
     */
    private void initTabAdapter() {
        initing(getString(R.string.order_fragment_loading));
        mTabTableAdapter = new TabTableAdapter(mContext, null);
        mTabTableAdapter.setOnItemClickListener(this);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        dishesTab.setLayoutManager(mLayoutManager);
        dishesTab.setAdapter(mTabTableAdapter);
    }

    /**
     * 初始化菜品适配器
     */
    private void initFoodAdapter() {
        mFoodAdapter = new FoodAdapter(mContext, null);
        dishesShow.setLayoutManager(new LinearLayoutManager(mContext));
        dishesShow.setAdapter(mFoodAdapter);
        dishesShow.addItemDecoration(
                new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.line_s), 100000, 1));
    }

    /**
     * 初始化分类数据
     */
    private void initCate() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setOrderType(orderType);
        mFoodMode.cates(bean, cateCallback);
    }

    /**
     * 初始化导航栏数据
     */
    private void initTab() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setOrderType(RequiredFagment.orderType);
        mTableMode.info(bean, tableInfoCallback);
    }

    /**
     * 初始化菜品数据
     */
    private void initFood(int cateId) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setCateId(cateId);
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        mFoodMode.foods(bean, foodCallback);
    }

    /**
     * 分类回调
     */
    Callback<Bean<CateBean>> cateCallback = new Callback<Bean<CateBean>>() {
        @Override
        public void onResponse(Call<Bean<CateBean>> call, Response<Bean<CateBean>> response) {
            Bean<CateBean> bean = response.body();
            if (bean != null) {
                if (bean.getCode() == ResponseCode.SUCCESS) {
                    initSuccess();
                    /**加载成功*/
                    defaultItem = bean.getResult().getDefaultCate();//设置默认显示
                    defaultItem = currPosition != -1 ? currPosition : defaultItem;
                    if (currPosition >= bean.getResult().getCates().size()) {
                        /**如果分类减少，处理越界*/
                        defaultItem = bean.getResult().getDefaultCate();//设置默认显示
                    }
                    mCatesAdapter.setCurrent(defaultItem);
                    mCatesAdapter.notifyDataSetChanged(bean.getResult().getCates());
                    /**请求指定分类菜品*/
                    currBean = bean.getResult().getCates().get(defaultItem);
                    onItemClick(null, currBean, defaultItem);
                } else if (bean.getCode() == ResponseCode.FAILURE) {
                    initFailer(bean.getMessage());
                }
            } else {
                initFailer(mContext.getResources().getString(R.string.order_fragment_loading_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<CateBean>> call, Throwable t) {
            initFailer(mContext.getResources().getString(R.string.order_fragment_loading_fail));
        }
    };

    /**
     * 台桌信息
     */
    Callback<Bean<List<InfoBean>>> tableInfoCallback = new Callback<Bean<List<InfoBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<InfoBean>>> call, Response<Bean<List<InfoBean>>> response) {
            Bean<List<InfoBean>> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                /**加载成功*/
                setTabContext(bean.getResult());
                mTabTableAdapter.setCurrTableId(tableId);
                mTabTableAdapter.notifyDataSetChanged(bean.getResult());
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                initFailer(bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<List<InfoBean>>> call, Throwable t) {
            initFailer(getString(R.string.order_fragment_loading_fail));
        }
    };

    /**
     * 获取菜品回调
     */
    Callback<Bean<List<FoodBean>>> foodCallback = new Callback<Bean<List<FoodBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<FoodBean>>> call, Response<Bean<List<FoodBean>>> response) {
            Bean<List<FoodBean>> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                /**菜品请求成功*/
                /**处理适配数据*/
                foodList = FoodRefreshImpl.getInstance().refreshFood(disOrderList, bean.getResult());
                mFoodAdapter.notifyDataSetChanged(foodList);
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                initFailer(bean.getMessage());
            }
        }

        @Override
        public void onFailure(Call<Bean<List<FoodBean>>> call, Throwable t) {
            initFailer(getString(R.string.order_fragment_loading_fail));
        }
    };

    /**
     * 设置台桌显示内容
     */
    private void setTabContext(List<InfoBean> mList) {
        if (mList == null || mList.size() == 0) {
            /**单桌显示方式（小于2桌只有一桌的只有单独点餐）*/
            tabLayout(false);
            tabText.setText(getString(R.string.order_fragment_one_order));
        } else if (mList.size() == 1) {
            tabLayout(false);
            if (mList.get(0).getCombine() == Constant.MERGE_TABLE) {
                tabText.setText(getString(R.string.order_fragment_more_order));
            } else {
                tabText.setText(getString(R.string.order_fragment_one_order));
            }
        } else {
            /**多桌显示方式*/
            for (InfoBean item : mList) {
                if (item.getCombine() == Constant.MERGE_TABLE) {
                    /**合并点餐*/
                    if (tableId == item.getId() && item.getConsumeType() == Constant.TABLE_TYPE_AUX) {
                        /**并桌的辅桌（显示并桌点餐）*/
                        tabLayout(false);
                        tabText.setText(getString(R.string.order_fragment_more_order));
                        return;
                    } else if (tableId == item.getId() && item.getConsumeType() == Constant.TABLE_TYPE_MAIN) {
                        /**并桌的主桌（显示列表）*/
                        tabLayout(true);
                        return;
                    }
                } else if (item.getCombine() == Constant.NON_MERGE_TABLE) {
                    /**单独点餐*/
                    tabLayout(false);
                    tabText.setText(getString(R.string.order_fragment_one_order));
                }

            }
        }
    }

    /**
     * 台桌信息显示
     *
     * @param isShow 是否显示列表
     */
    private void tabLayout(boolean isShow) {
        if (isShow) {
            dishesTab.setVisibility(View.VISIBLE);
            tabText.setVisibility(View.GONE);
        } else {
            dishesTab.setVisibility(View.GONE);
            tabText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        /**反注册-事件监听*/
        EventBus.getDefault().unregister(this);
    }

    /**
     * 一级分类被点击
     *
     * @param view     视图
     * @param bean     一级分类对象
     * @param position 点击下标
     */

    @Override
    public void onItemClick(View view, Object bean, int position) {
        if (bean != null) {
            currPosition = position;
            currBean = bean;
            CateBean.CateItemBean itemBean = (CateBean.CateItemBean) bean;
            /**设置是否必选*/
            mFoodAdapter.setRequired(itemBean.getRequired());
            /**设置最大数量*/
            mFoodAdapter.setMaximum(itemBean.getMaximum());
            /**设置分类权限*/
            mFoodAdapter.setCatePermiss(itemBean.getPermiss());
            /**设置分类ID*/
            mFoodAdapter.setCateId(itemBean.getId());
            move(position);
            initFood(itemBean.getId());
        }
    }

    /**
     * 当点击某一Item时，当前Item滚动到顶部
     *
     * @param n
     */
    private void move(int n) {
        if (n < 0 || n >= mCatesAdapter.getItemCount()) {
            return;
        }
        dishesClassify.stopScroll();
        moveToPosition(n);
    }

    private void moveToPosition(int n) {

        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            dishesClassify.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = dishesClassify.getChildAt(n - firstItem).getTop();
            dishesClassify.scrollBy(0, top);
        } else {
            dishesClassify.scrollToPosition(n);
        }

    }

    /**
     * 初始化PopWindow
     */
    private void initPop() {
        popView = LayoutInflater.from(mContext).inflate(R.layout.pop_more_dishes, null);
        mPopupWindow = new PopupWindow(popView, ConvertUtils.dip2px(mContext, POP_WIDTH), ConvertUtils.dip2px(mContext, POP_HEIGHT), true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        ((CheckBox) (popView.findViewById(R.id.pop_isHint))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setSelected(true);
                    isHint = false;
                } else {
                    buttonView.setSelected(false);
                    isHint = true;
                }
            }
        });
        popView.findViewById(R.id.pop_ikonw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * Tab栏点击
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onItemClick(View view, int position, Object bean) {

    }

    /**
     * 菜品数量比较，如果过多，显示提示框
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void moreDishesHintEventBus(MoreDishesHintEvent event) {
        int number = event.getNumber();
        if (isHint && isAddFood) {
            if (number > warmPromptNumber) {
                mPopupWindow.showAtLocation(popView, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.6f);
            }
        }
    }

    /**
     * 购物车数据提交了
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshFoodBus(RefreshFoodEvent event) {
        if (event.getType() == RefreshFoodEvent.CART_COMMIT) {
            onItemClick(null, currBean, currPosition);
        } else if (event.getType() == RefreshFoodEvent.FAVOR_FOOD) {
            /**点赞*/
            onItemClick(null, currBean, currPosition);
        } else if (event.getType() == RefreshFoodEvent.CART_MINUS_FOOD) {
            /**购物车的减菜*/
            mFoodAdapter.notifyDataSetChanged(
                    FoodRefreshImpl.getInstance().refreshFood(event.getBean(), foodList));
        } else if (event.getType() == RefreshFoodEvent.DETAILS_ADD_FOOD) {
            /**详情的加菜*/
            mFoodAdapter.notifyDataSetChanged(
                    FoodRefreshImpl.getInstance().refreshFood(event.getBean(), foodList));
        } else if (event.getType() == RefreshFoodEvent.DETAILS_MINUS_FOOD) {
            /**详情的减菜*/
            mFoodAdapter.notifyDataSetChanged(
                    FoodRefreshImpl.getInstance().refreshFood(event.getBean(), foodList));
        }
    }

    /**
     * 通知事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketEventBus(WebSocketEvent event) {
        if (WebSocketEvent.REFRESH_FOOD == event.getType()) {
            /**刷新菜品数据--菜品列表，已提交菜品（在购物车界面刷新），已点未提交菜品（在购物车界面刷新）*/
//            onItemClick(null, currBean, currPosition);
            initCate();//刷新分类列表,默认刷新当前分类下的菜品

        } else if (WebSocketEvent.ALONE_ORDER == event.getType()) {
            /**单独点餐*/
            orderType = Constant.ORDER_TYPE_ALONE;
            MyApplication.tableBean.setOrderType(orderType);
            tabLayout(false);
            tabText.setText(getString(R.string.order_fragment_one_order));
            initCate();//刷新分类列表,默认刷新当前分类下的菜品
        } else if (WebSocketEvent.MERGE_TABLE == event.getType()) {
            /**并桌*/
            orderType = Constant.ORDER_TYPE_MERGE;
            MyApplication.tableBean.setOrderType(orderType);
            initTab();//刷新台桌
            initCate();//刷新分类列表,默认刷新当前分类下的菜品

        }
    }

}
