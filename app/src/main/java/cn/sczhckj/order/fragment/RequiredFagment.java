package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.FoodAdapter;
import cn.sczhckj.order.adapter.PersonAdapter;
import cn.sczhckj.order.adapter.TabAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.CateBean;
import cn.sczhckj.order.data.bean.Constant;
import cn.sczhckj.order.data.bean.FoodBean;
import cn.sczhckj.order.data.bean.OpenInfoBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.CartNumberEvent;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 必选菜品界面
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class RequiredFagment extends BaseFragment implements Callback<Bean<OpenInfoBean>>, OnItemClickListener {

    @Bind(R.id.person_choose)
    RecyclerView personChoose;
    @Bind(R.id.dishes_choose)
    RecyclerView dishesChoose;

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
    @Bind(R.id.required_tab)
    RecyclerView requiredTab;

    /**
     * 开桌数据信息请求
     */
    private TableMode mTableMode;
    /**
     * 获取菜品信息
     */
    private FoodMode mFoodMode;

    /**
     * 默认显示几列备选人数
     */
    public static int DEFAULT_PERSON = 3;

    /**
     * 台桌信息监听
     */
    private OnTableListenner mOnTableListenner;
    /**
     * 用户ID
     */
    private String userId = "";
    /**
     * 导航栏适配器
     */
    private TabAdapter mTabAdapter;
    /**
     * 就餐人数适配器
     */
    private PersonAdapter personAdapter;
    /**
     * 菜品适配器
     */
    private FoodAdapter mFoodAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**注册事件监听*/
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_required, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initAdapter();
    }

    @Override
    public void init() {
        mTableMode = new TableMode();
        mFoodMode = new FoodMode();
        initTableInfo();
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        /**Tab栏*/
        mTabAdapter = new TabAdapter(getContext(), null);
        mTabAdapter.setOnItemClickListener(this);
        LinearLayoutManager tabManager = new LinearLayoutManager(getContext());
        tabManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        requiredTab.setLayoutManager(tabManager);
        requiredTab.setAdapter(mTabAdapter);
        /**就餐人数选择*/
        personAdapter = new PersonAdapter(getContext(), null);
        personChoose.setLayoutManager(new LinearLayoutManager(getContext()));
        personChoose.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.hint_color_dash), 5, 1, DEFAULT_PERSON));
        personChoose.setAdapter(personAdapter);
        personAdapter.setOnTableListenner(mOnTableListenner);
        /**菜品*/
        mFoodAdapter = new FoodAdapter(getContext(), null);
        dishesChoose.setLayoutManager(new LinearLayoutManager(getContext()));
        dishesChoose.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(getContext(), R.color.line_s), 100000, 1));
        dishesChoose.setAdapter(mFoodAdapter);

    }

    /**
     * 初始化开桌信息
     */
    private void initTableInfo() {
        deviceId = AppSystemUntil.getAndroidID(getContext());
        RequestCommonBean bean = new RequestCommonBean();
        bean.setUserId(userId);
        bean.setDeviceId(deviceId);
        /**获取数据*/
        mTableMode.openInfo(bean, this);
        loading(getContext().getResources().getString(R.string.loading));
        initTab();
    }

    /**
     * 初始化导航栏
     */
    private void initTab() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setOrderType(Constant.ORDER_TYPE_OPEN);
        mFoodMode.cates(bean, cateCallback);
    }

    /**
     * 初始化菜品列表
     *
     * @param cateId
     */
    private void initFood(int cateId) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setCateId(cateId);
        mFoodMode.foods(bean, foodCallback);
    }

    /**
     * 设置默认显示就餐人数选择
     *
     * @param mList
     */
    private void defaultPerson(List<Integer> mList) {
        if (mList != null) {
            DEFAULT_PERSON = mList.size();
            DEFAULT_PERSON = DEFAULT_PERSON >= 0 ? DEFAULT_PERSON : 0;
        } else {
            DEFAULT_PERSON = 0;
        }
    }

    /**
     * 菜品分类回调
     */
    private Callback<Bean<CateBean>> cateCallback = new Callback<Bean<CateBean>>() {
        @Override
        public void onResponse(Call<Bean<CateBean>> call, Response<Bean<CateBean>> response) {

            Bean<CateBean> bean = response.body();
            if (bean != null) {
                if (bean.getCode() == ResponseCode.SUCCESS) {
                    mTabAdapter.setDefaultItem(bean.getResult().getDefaultCate() != null ? bean.getResult().getDefaultCate() : 0);
                    cateList=bean.getResult().getCates();
                    mTabAdapter.notifyDataSetChanged(cateList);
                } else {
                    loadingFail(getContext().getResources().getString(R.string.loadingFail));
                }
            } else {
                loadingFail(getContext().getResources().getString(R.string.loadingFail));
            }
        }

        @Override
        public void onFailure(Call<Bean<CateBean>> call, Throwable t) {
            loadingFail(getContext().getResources().getString(R.string.loadingFail));
        }
    };

    /**
     * 菜品列表回调
     */
    private Callback<Bean<List<FoodBean>>> foodCallback = new Callback<Bean<List<FoodBean>>>() {
        @Override
        public void onResponse(Call<Bean<List<FoodBean>>> call, Response<Bean<List<FoodBean>>> response) {
            Bean<List<FoodBean>> bean = response.body();
            if (bean != null) {
                if (bean.getCode() == ResponseCode.SUCCESS) {
                    loadingSuccess();
                    mFoodAdapter.notifyDataSetChanged(bean.getResult());
                } else {
                    loadingFail(getContext().getResources().getString(R.string.loadingFail));
                }
            } else {
                loadingFail(getContext().getResources().getString(R.string.loadingFail));
            }
        }

        @Override
        public void onFailure(Call<Bean<List<FoodBean>>> call, Throwable t) {
            loadingFail(getContext().getResources().getString(R.string.loadingFail));
        }
    };

    @Override
    public void setData(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        /**取消事件监听*/
        EventBus.getDefault().unregister(this);
    }

    /**
     * 购物车数据变化后，通知菜品变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {

    }

    @OnClick({R.id.loadingParent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadingParent:
                /**点击重新加载数据*/
                initTableInfo();
                break;
        }
    }

    public void setOnTableListenner(OnTableListenner mOnTableListenner) {
        this.mOnTableListenner = mOnTableListenner;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onResponse(Call<Bean<OpenInfoBean>> call, Response<Bean<OpenInfoBean>> response) {
        Bean<OpenInfoBean> bean = response.body();
        if (bean != null) {
            if (bean.getCode() == ResponseCode.SUCCESS) {
                /**设置桌面桌号、服务员、就餐人数*/
                mOnTableListenner.table(bean.getResult().getTableName());
                mOnTableListenner.waiter(bean.getResult().getWaiter());
                mOnTableListenner.person(bean.getResult().getMaximum());
                /**设置就餐人数选择*/
                defaultPerson(bean.getResult().getPersons());
                List<Integer> mList = bean.getResult().getPersons();
                mList.add(bean.getResult().getMaximum());
                personAdapter.notifyDataSetChanged(mList);
            } else {
                loadingFail(bean.getMessage());
            }
        } else {
            loadingFail(getContext().getResources().getString(R.string.loadingFail));
        }
    }

    @Override
    public void onFailure(Call<Bean<OpenInfoBean>> call, Throwable t) {
        loadingFail(getContext().getResources().getString(R.string.loadingFail));
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

    /**
     * Tab导航栏Item点击
     *
     * @param view
     * @param position
     * @param bean
     */
    @Override
    public void onItemClick(View view, int position, Object bean) {
        initFood(((CateBean.CateItemBean) bean).getId());
    }
}
