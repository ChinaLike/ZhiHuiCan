package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.FoodAdapter;
import cn.sczhckj.order.adapter.PersonAdapter;
import cn.sczhckj.order.adapter.TabCateAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.food.CateBean;
import cn.sczhckj.order.data.bean.food.FoodBean;
import cn.sczhckj.order.data.bean.table.TableBean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.event.RefreshFoodEvent;
import cn.sczhckj.order.data.event.WebSocketEvent;
import cn.sczhckj.order.data.listener.OnItemClickListener;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.FoodMode;
import cn.sczhckj.order.mode.TableMode;
import cn.sczhckj.order.mode.impl.FoodRefreshImpl;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.L;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 必选菜品界面
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class RequiredFagment extends BaseFragment implements Callback<Bean<TableBean>>, OnItemClickListener {

    @Bind(R.id.person_choose)
    RecyclerView personChoose;
    @Bind(R.id.dishes_choose)
    RecyclerView dishesChoose;
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
    private TabCateAdapter mTabCateAdapter;
    /**
     * 就餐人数适配器
     */
    private PersonAdapter personAdapter;
    /**
     * 菜品适配器
     */
    private FoodAdapter mFoodAdapter;
    /**
     * 每一次点击菜品集合
     */
    private List<FoodBean> foodList = new ArrayList<>();
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

    @Override
    public int setLayoutId() {
        return R.layout.fragment_required;
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

    @Override
    public void initFail() {
        /**点击重新加载数据*/
        initTableInfo();
    }

    @Override
    public void loadingFail() {

    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        /**Tab栏*/
        mTabCateAdapter = new TabCateAdapter(mContext, null);
        mTabCateAdapter.setOnItemClickListener(this);
        LinearLayoutManager tabManager = new LinearLayoutManager(mContext);
        tabManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        requiredTab.setLayoutManager(tabManager);
        requiredTab.setAdapter(mTabCateAdapter);
        /**就餐人数选择*/
        personAdapter = new PersonAdapter(mContext, null);
        personChoose.setLayoutManager(new LinearLayoutManager(mContext));
        personChoose.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.hint_color_dash), 5, 1, DEFAULT_PERSON));
        personChoose.setAdapter(personAdapter);
        personAdapter.setOnTableListenner(mOnTableListenner);
        /**菜品*/
        mFoodAdapter = new FoodAdapter(mContext, null);
        dishesChoose.setLayoutManager(new LinearLayoutManager(mContext));
        dishesChoose.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.line_s), 100000, 1));
        dishesChoose.setAdapter(mFoodAdapter);

    }

    /**
     * 初始化开桌信息
     */
    private void initTableInfo() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setUserId(userId);
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        /**获取数据*/
        mTableMode.openInfo(bean, this);
        initing(getString(R.string.require_fragment_loading));
        initTab();
    }

    /**
     * 初始化导航栏
     */
    private void initTab() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
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
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
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
                    mTabCateAdapter.setDefaultItem(bean.getResult().getDefaultCate() != null ? bean.getResult().getDefaultCate() : 0);
                    cateList = bean.getResult().getCates();
                    mTabCateAdapter.notifyDataSetChanged(cateList);
                } else {
                    initFailer(bean.getMessage());
                }
            } else {
                initFailer(getString(R.string.require_fragment_loading_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<CateBean>> call, Throwable t) {
            initFailer(getString(R.string.require_fragment_loading_fail));
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
                    initSuccess();
                    foodList = FoodRefreshImpl.getInstance().refreshFood(disOrderList, bean.getResult());
                    mFoodAdapter.notifyDataSetChanged(foodList);
                } else {
                    initFailer(bean.getMessage());
                }
            } else {
                initFailer(getString(R.string.require_fragment_loading_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<List<FoodBean>>> call, Throwable t) {
            initFailer(getString(R.string.require_fragment_loading_fail));
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

    public void setOnTableListenner(OnTableListenner mOnTableListenner) {
        this.mOnTableListenner = mOnTableListenner;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onResponse(Call<Bean<TableBean>> call, Response<Bean<TableBean>> response) {
        Bean<TableBean> bean = response.body();

        if (bean != null) {
            if (bean.getCode() == ResponseCode.SUCCESS) {
                L.d("数据初始化：Requied="+bean.getResult().toString());
                /**设置桌面桌号、服务员、就餐人数*/
                tableId = bean.getResult().getId();
                mOnTableListenner.table(bean.getResult().getTableName());
                mOnTableListenner.waiter(bean.getResult().getWaiter());
                mOnTableListenner.person(bean.getResult().getMaximum());
                /**设置就餐人数选择*/
                defaultPerson(bean.getResult().getPersons());
                List<Integer> mList = bean.getResult().getPersons();
                mList.add(bean.getResult().getMaximum());
                personAdapter.notifyDataSetChanged(mList);
            } else {
                initFailer(bean.getMessage());
            }
        } else {
            initFailer(getString(R.string.require_fragment_loading_fail));
        }
    }

    @Override
    public void onFailure(Call<Bean<TableBean>> call, Throwable t) {
        initFailer(getString(R.string.require_fragment_loading_fail));
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
        if (bean != null) {
            currPosition = position;
            currBean = bean;
            CateBean.CateItemBean itemBean = (CateBean.CateItemBean) bean;
            mFoodAdapter.setRequired(itemBean.getRequired());
            mFoodAdapter.setMaximum(itemBean.getMaximum());
            mFoodAdapter.setCatePermiss(itemBean.getPermiss());
            mFoodAdapter.setCateId(itemBean.getId());
            initFood(itemBean.getId());
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
            onItemClick(null, currPosition, currBean);
        } else if (event.getType() == RefreshFoodEvent.FAVOR_FOOD) {
            /**点赞*/
            onItemClick(null, currPosition, currBean);
        } else if (event.getType() == RefreshFoodEvent.CART_MINUS_FOOD) {
            /**购物车发出菜品减少*/
            mFoodAdapter.notifyDataSetChanged(
                    FoodRefreshImpl.getInstance().refreshFood(event.getBean(), foodList));
        } else if (event.getType() == RefreshFoodEvent.DETAILS_MINUS_FOOD) {
            /**详情发出菜品减少*/
            mFoodAdapter.notifyDataSetChanged(
                    FoodRefreshImpl.getInstance().refreshFood(event.getBean(), foodList));
        } else if (event.getType() == RefreshFoodEvent.DETAILS_ADD_FOOD) {
            /**详情发出菜品增加*/
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
            /**刷新菜品*/
            initTab();//刷新菜品分类列表，同时刷新了默认分类下数据
//            onItemClick(null, currPosition, currBean);//刷新菜品数据
        } else if (WebSocketEvent.ALONE_ORDER == event.getType()) {
            /**单独点餐*/
            initTab(); //刷新分类列表
//            onItemClick(null, currPosition, currBean);//刷新菜品数据
        }
    }
}
