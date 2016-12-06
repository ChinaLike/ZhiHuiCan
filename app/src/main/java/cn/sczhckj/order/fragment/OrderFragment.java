package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.ClassifyAdapter;
import cn.sczhckj.order.adapter.TabAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.ClassifyBean;
import cn.sczhckj.order.data.bean.ClassifyItemBean;
import cn.sczhckj.order.data.bean.DishesBean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.TabBean;
import cn.sczhckj.order.data.event.CartNumberEvent;
import cn.sczhckj.order.data.event.MoreDishesHintEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.ConvertUtils;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 点餐界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class OrderFragment extends BaseFragment implements Callback<Bean<ClassifyBean>>, ClassifyAdapter.OnItemClickListener {


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
     * 分类适配器
     */
    private ClassifyAdapter mClassifyAdapter;
    /**
     * 默认显示第几个
     */
    private int defaultItem = 0;

    private LinearLayoutManager mLinearLayoutManager;

    private int mIndex = 0;

    private List<ClassifyItemBean> classifyList;
    /**
     * 装菜品容器，如果有数据，直接刷新，如果没有重新请求数据
     */
    private Map<String, List<DishesBean>> dishesMap = new HashMap<>();

    /**
     * 头部导航栏数据
     */
    private List<TabBean> tabList = new ArrayList<>();

    /**
     * 导航栏适配器
     */
    private TabAdapter mTabAdapter;
    /**
     * 点菜桌选择类型，0-主桌
     */
    public static int tabOrderType = 0;

    /**
     * 菜品过多温馨提示界线
     */
    private int warmPromptNumber = 15;

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
     * pop的X轴坐标
     */
    private float X = 0;
    /**
     * pop的Y轴坐标
     */
    private float Y = 0;
    /**
     * 当前请求的数据
     */
    private ClassifyItemBean currentBean;

    private boolean isMainTable = true;

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
        initRect();
        init();
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 初始化Pop的位置
     */
    private void initRect() {
        int screenHeight = AppSystemUntil.height(getContext());
        int screenWidth = AppSystemUntil.width(getContext());
        Y = (screenHeight - ConvertUtils.dip2px(getContext(), POP_HEIGHT)) / 2;
        X = screenWidth * 18 / 26 - ConvertUtils.dip2px(getContext(), POP_WIDTH) / 2;
    }

    @Override
    public void init() {
        parentDishesList = null;
        initDishesAdapter(dishesShow);
        initTabAdapter();
        /**初始化PopWindow*/
        initPop();
    }

    /**
     * 加载菜单分类项
     *
     * @param type
     */
    public void loadingClassify(int type) {
        this.orderType = type;
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setOrderType(type);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ORDER_CLASSIFY_SHOW)
                .time()
                .bean(bean);

        Call<Bean<ClassifyBean>> classify = RetrofitRequest.service().classify(restRequest.toRequestString());
        classify.enqueue(this);
        loading(getContext().getResources().getString(R.string.loading));
    }

    @Override
    public void onResponse(Call<Bean<ClassifyBean>> call, Response<Bean<ClassifyBean>> response) {
        Bean<ClassifyBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {

            defaultItem = bean.getResult().getDefaultClassify();
            List<ClassifyItemBean> itemBeen = bean.getResult().getClassifyItemList();
            classifyList = itemBeen;
            ClassifyItemBean item = itemBeen.get(defaultItem);
            item.setSelect(true);
            initClassify(itemBeen);
            initDishesRequest(itemBeen.get(defaultItem));
            /**是否是主桌，不是主桌开启服务实时刷新数据*/
            isMainTable(bean.getResult());
            /**初始化头部导航栏数据*/
            initTab(bean.getResult().getTabList());
            /**菜品过多提示数量*/
            warmPromptNumber = bean.getResult().getOrderMoreHint();

        }
    }

    /**
     * 是否是主桌
     *
     * @param bean
     */
    private void isMainTable(ClassifyBean bean) {
        /**验证不是主桌*/
        if (bean.getMainTale() != 0) {
            isMainTable = false;
            startCartService();
        }
    }

    @Override
    public void onFailure(Call<Bean<ClassifyBean>> call, Throwable t) {
        loadingFail(getContext().getResources().getString(R.string.loadingFail));
    }

    /**
     * 初始化分类
     *
     * @param itemBeen
     */
    private void initClassify(List<ClassifyItemBean> itemBeen) {
        mClassifyAdapter = new ClassifyAdapter(getContext(), itemBeen, defaultItem);
        mClassifyAdapter.addOnItemClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        dishesClassify.setLayoutManager(mLinearLayoutManager);
        dishesClassify.setAdapter(mClassifyAdapter);
        dishesClassify.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 5, 1));
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
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        defaultItem = position;
        move(position);

//        if (dishesMap.containsKey(classifyList.get(position).getName())) {
//            /**再次验证一级分类ID与之对应*/
//            if (dishesMap.get(classifyList.get(position).getName()).size() > 0 &&
//                    (dishesMap.get(classifyList.get(position).getName()).get(0).getType()
//                            == classifyList.get(position).getId())) {
//                /**如果存在已经请求数据，则直接利用*/
//                parentDishesList = dishesMap.get(classifyList.get(position).getName());
//                mDishesAdapter.notifyDataSetChanged(parentDishesList);
//            } else {
//                initDishesRequest(classifyList.get(position));
//            }
//        } else {
        initDishesRequest(classifyList.get(position));
//        }
    }

    /**
     * 初始化菜品请求数据
     *
     * @param bean
     */
    private void initDishesRequest(ClassifyItemBean bean) {
        currentBean = bean;
        int id = bean.getId();
        String url = bean.getUrl();
        loadingDishes(url, id);
    }

    /**
     * 加载菜单
     *
     * @param url
     * @param id
     */
    private void loadingDishes(String url, int id) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setId(id + "");
        bean.setDeviceId(deviceId);
        bean.setOrderType(orderType);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ORDER_DISHES_SHOW)
                .time()
                .bean(bean);

        Call<Bean<List<DishesBean>>> dishesBeanCall = RetrofitRequest.service().dishes(restRequest.toRequestString());
        dishesBeanCall.enqueue(new Callback<Bean<List<DishesBean>>>() {
            @Override
            public void onResponse(Call<Bean<List<DishesBean>>> call, Response<Bean<List<DishesBean>>> response) {
                Bean<List<DishesBean>> listBean = response.body();
                if (listBean != null && listBean.getCode() == ResponseCode.SUCCESS) {
                    /**在菜品清单请求成功后才做显示*/
                    loadingSuccess();

                    dishesMap.put(classifyList.get(defaultItem).getName(), listBean.getResult());
                    parentDishesList = listBean.getResult();
                    mDishesAdapter.notifyDataSetChanged(listBean.getResult());
                } else if (listBean != null) {
                    loadingFail(listBean.getMessage());
                } else {
                    loadingFail(getContext().getResources().getString(R.string.loadingFail));
                }
            }

            @Override
            public void onFailure(Call<Bean<List<DishesBean>>> call, Throwable t) {
                loadingFail(getContext().getResources().getString(R.string.loadingFail));
            }
        });
    }

    /**
     * 初始化导航栏
     *
     * @param tabList
     */
    private void initTab(List<TabBean> tabList) {
        if (tabList == null || tabList.size() == 0 || !isMainTable) {
            dishesTab.setVisibility(View.GONE);
            tabText.setVisibility(View.VISIBLE);
            if (orderType == ALONE_ORDER) {
                tabOrderType = 0;
                tabText.setText(getResources().getString(R.string.one_order));
            } else {
                tabOrderType = 1;
                tabText.setText(getResources().getString(R.string.more_order));
            }
        } else {
            dishesTab.setVisibility(View.VISIBLE);
            tabText.setVisibility(View.GONE);
            this.tabList = tabList;
            mTabAdapter.notifyDataSetChanged(this.tabList);
            tabOrderType = tabList.get(0).getId();
        }
    }

    private void move(int n) {
        if (n < 0 || n >= mClassifyAdapter.getItemCount()) {
            return;
        }
        mIndex = n;
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
     * 购物车数据变化监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {
        DishesBean bean = event.getBean();
        if (bean != null) {
            String id = bean.getId();
            String dishesName = bean.getName();
            for (DishesBean item : parentDishesList) {
                if (item.getId().equals(id) && item.getName().equals(dishesName)) {
                    item.setNumber(bean.getNumber());
                    mDishesAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 初始化头部导航栏适配器
     */
    private void initTabAdapter() {
        mTabAdapter = new TabAdapter(getContext(), tabList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dishesTab.setLayoutManager(mLayoutManager);
        dishesTab.setAdapter(mTabAdapter);
    }

    /**
     * 菜品数量比较，如果过多，显示提示框
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void moreDishesHintEventBus(MoreDishesHintEvent event) {
        int number = event.getNumber();
        if (isHint) {
            if (number > warmPromptNumber) {
                mPopupWindow.showAtLocation(popView, Gravity.NO_GRAVITY, (int) X, (int) Y);
                backgroundAlpha(0.6f);
            }
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
        popView.findViewById(R.id.pop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                isHint = false;
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
     * 加载中
     */
    private void loading(String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
        loadingTitle.setText(str);
    }

    /**
     * 加载成功
     */
    private void loadingSuccess() {
        loadingParent.setVisibility(View.GONE);
        contextParent.setVisibility(View.VISIBLE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    private void loadingFail(String str) {
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.GONE);
        loadingFail.setVisibility(View.VISIBLE);
        loadingFailTitle.setText(str);
    }


    @OnClick(R.id.loadingParent)
    public void onClick() {
        /**加载失败时使用*/
        initDishesRequest(currentBean);
    }
}
