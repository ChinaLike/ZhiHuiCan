package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.CatesAdapter;
import cn.sczhckj.order.adapter.FoodAdapter;
import cn.sczhckj.order.adapter.TabTableAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.table.InfoBean;
import cn.sczhckj.order.data.event.MoreDishesHintEvent;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.FoodRefreshImpl;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 点餐界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class OrderFragment extends BaseFragment implements CatesAdapter.OnItemClickListener, OnItemClickListener {


    private final String TAG = getClass().getSimpleName();


    @Bind(R.id.dishes_tab)
    RecyclerView dishesTab;
    @Bind(R.id.dishes_classify)
    RecyclerView dishesClassify;
    @Bind(R.id.dishes_show)
    RecyclerView dishesShow;
    @Bind(R.id.tab_text)
    TextView tabText;
    @Bind(R.id.contextParent)
    LinearLayout contextParent;
    @Bind(R.id.loading_title)
    TextView loadingTitle;
    @Bind(R.id.loading_parent)
    LinearLayout loadingItemParent;
    @Bind(R.id.loading_fail_title)
    TextView loadingFailTitle;
    @Bind(R.id.loading_fail)
    LinearLayout loadingFail;
    @Bind(R.id.loadingParent)
    LinearLayout loadingParent;

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
    private int currPosition;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void init() {
        isOpen = true;
        initLoading(true);
        initCateAdapter();
        initTabAdapter();
        initFoodAdapter();
        mFoodMode = new FoodMode();
        mTableMode = new TableMode();
        initCate();
        initTab();
    }

    @Override
    public void setData(Object object) {

    }

    /**
     * 初始化进度加载框
     *
     * @param isShow 是否显示
     */
    private void initLoading(boolean isShow) {
        if (isShow) {
            loadingParent.setVisibility(View.VISIBLE);
            contextParent.setVisibility(View.GONE);
        } else {
            loadingParent.setVisibility(View.GONE);
            contextParent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化分类适配器
     */
    private void initCateAdapter() {
        mCatesAdapter = new CatesAdapter(getContext(), null, defaultItem);
        /**添加Item点击监听*/
        mCatesAdapter.addOnItemClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        dishesClassify.setLayoutManager(mLinearLayoutManager);
        dishesClassify.setAdapter(mCatesAdapter);
        dishesClassify.addItemDecoration(
                new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.line_s), 5, 1));
    }

    /**
     * 初始化头部Tab适配器
     */
    private void initTabAdapter() {
        mTabTableAdapter = new TabTableAdapter(getContext(), null);
        mTabTableAdapter.setOnItemClickListener(this);
        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dishesTab.setLayoutManager(mLayoutManager);
        dishesTab.setAdapter(mTabTableAdapter);
    }

    /**
     * 初始化菜品适配器
     */
    private void initFoodAdapter() {
        mFoodAdapter = new FoodAdapter(getContext(), null);
        dishesShow.setLayoutManager(new LinearLayoutManager(getContext()));
        dishesShow.setAdapter(mFoodAdapter);
        dishesShow.addItemDecoration(
                new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.line_s), 100000, 1));
    }

    /**
     * 初始化分类数据
     */
    private void initCate() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setOrderType(orderType);
        mFoodMode.cates(bean, cateCallback);
    }

    /**
     * 初始化导航栏数据
     */
    private void initTab() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setOrderType(RequiredFagment.orderType);
        mTableMode.info(bean, tableInfoCallback);
    }

    /**
     * 初始化菜品数据
     */
    private void initFood(int cateId) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(getContext()));
        bean.setCateId(cateId);
        bean.setMemberCode(MyApplication.memberCode);
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
                    initLoading(false);
                    loadingSuccess(loadingParent, contextParent, loadingItemParent, loadingFail);
                    /**加载成功*/
                    defaultItem = bean.getResult().getDefaultCate();//设置默认显示
                    mCatesAdapter.setCurrent(defaultItem);
                    mCatesAdapter.notifyDataSetChanged(bean.getResult().getCates());
                    /**请求默认*/
                    initFood(bean.getResult().getCates().get(defaultItem).getId());
                } else {
                    loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                            getContext().getResources().getString(R.string.loadingFail));
                }
            } else {
                loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                        getContext().getResources().getString(R.string.loadingFail));
            }
        }

        @Override
        public void onFailure(Call<Bean<CateBean>> call, Throwable t) {
            loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                    getContext().getResources().getString(R.string.loadingFail));
        }
    };

    /**
     * 台桌信息
     */
    Callback<Bean<InfoBean>> tableInfoCallback = new Callback<Bean<InfoBean>>() {
        @Override
        public void onResponse(Call<Bean<InfoBean>> call, Response<Bean<InfoBean>> response) {
            Bean<InfoBean> bean = response.body();
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                /**加载成功*/
                setTabContext(bean.getResult());
                mTabTableAdapter.notifyDataSetChanged(bean.getResult().getTables());
            } else {
                loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                        getContext().getResources().getString(R.string.loadingFail));
            }
        }

        @Override
        public void onFailure(Call<Bean<InfoBean>> call, Throwable t) {
            loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                    getContext().getResources().getString(R.string.loadingFail));
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
                foodList = FoodRefreshImpl.getInstance().refreshFood(disOrderList,bean.getResult());
                mFoodAdapter.notifyDataSetChanged(foodList);
            } else {
                loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                        getContext().getResources().getString(R.string.loadingFail));
            }
        }

        @Override
        public void onFailure(Call<Bean<List<FoodBean>>> call, Throwable t) {
            loadingFail(loadingParent, contextParent, loadingItemParent, loadingFail, loadingFailTitle,
                    getContext().getResources().getString(R.string.loadingFail));
        }
    };

    /**
     * 设置台桌显示内容
     *
     * @param bean
     */
    private void setTabContext(InfoBean bean) {
        if (bean.getTableType() == Constant.TABLE_TYPE_ALONE) {
            /**单独点餐*/
            tabLayout(false);
            tabText.setText(getString(R.string.one_order));
        } else if (bean.getTableType() == Constant.TABLE_TYPE_MAIN) {
            /**主桌点餐*/
            tabLayout(true);
        } else if (bean.getTableType() == Constant.TABLE_TYPE_AUX) {
            /**辅桌点餐*/
            tabLayout(false);
            tabText.setText(getString(R.string.one_order));
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
        currPosition = position;
        currBean = bean;
        CateBean.CateItemBean itemBean = (CateBean.CateItemBean) bean;
        move(position);
        initFood(itemBean.getId());
        /**设置是否必选*/
        mFoodAdapter.setRequired(itemBean.getRequired());
        /**设置最大数量*/
        mFoodAdapter.setMaximum(itemBean.getMaximum());
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
        popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_more_dishes, null);
        mPopupWindow = new PopupWindow(popView, ConvertUtils.dip2px(getContext(), POP_WIDTH), ConvertUtils.dip2px(getContext(), POP_HEIGHT), true);
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

    @OnClick(R.id.loadingParent)
    public void onClick() {
        /**加载失败时使用*/
        init();
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
        }else if (event.getType() == RefreshFoodEvent.MINUS_FOOD){
            mFoodAdapter.notifyDataSetChanged(FoodRefreshImpl.getInstance().refreshFood(event.getBean(),foodList));
        }
    }

}
