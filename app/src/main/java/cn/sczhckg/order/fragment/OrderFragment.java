package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.ClassifyAdapter;
import cn.sczhckg.order.adapter.TabAdapter;
import cn.sczhckg.order.data.bean.ClassifyBean;
import cn.sczhckg.order.data.bean.ClassifyItemBean;
import cn.sczhckg.order.data.bean.DishesBean;
import cn.sczhckg.order.data.bean.TabBean;
import cn.sczhckg.order.data.event.CartNumberEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
import cn.sczhckg.order.overwrite.DashlineItemDivider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 点餐界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class OrderFragment extends BaseFragment implements Callback<ClassifyBean>, ClassifyAdapter.OnItemClickListener {


    @Bind(R.id.dishes_tab)
    RecyclerView dishesTab;
    @Bind(R.id.dishes_classify)
    RecyclerView dishesClassify;
    @Bind(R.id.dishes_show)
    RecyclerView dishesShow;
    @Bind(R.id.tab_text)
    TextView tabText;

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
//    /**
//     * 点餐类型
//     */
//    private int orderType = 0;
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

    public static int tabOrderType = 0;

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
        init();
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void init() {
        parentDishesList = null;
        initDishesAdapter(dishesShow);
        initTabAdapter();
    }

    /**
     * 加载菜单分类项
     *
     * @param type
     */
    public void loadingClassify(int type) {
        this.orderType = type;
        Call<ClassifyBean> classify = RetrofitRequest.service(Config.HOST).classify(MainActivity.table, type);
        classify.enqueue(this);
    }

    @Override
    public void onResponse(Call<ClassifyBean> call, Response<ClassifyBean> response) {
        ClassifyBean bean = response.body();
        if (bean != null) {
            defaultItem = bean.getDefaultClassify();
            List<ClassifyItemBean> itemBeen = bean.getClassifyItemList();
            classifyList = itemBeen;
            ClassifyItemBean item = itemBeen.get(defaultItem);
            item.setSelect(true);
            initClassify(itemBeen);
            initDishesRequest(itemBeen.get(defaultItem));
            /**初始化头部导航栏数据*/
            initTab(bean.getTabList());

        }
    }

    @Override
    public void onFailure(Call<ClassifyBean> call, Throwable t) {
        Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化分类
     *
     * @param itemBeen
     */
    private void initClassify(List<ClassifyItemBean> itemBeen) {
        mClassifyAdapter = new ClassifyAdapter(getContext(), itemBeen);
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

    @Override
    public void onItemClick(View view, int position) {
        defaultItem = position;
        move(position);
        if (dishesMap.containsKey(classifyList.get(position).getName())) {
            parentDishesList = dishesMap.get(classifyList.get(position).getName());
            mDishesAdapter.notifyDataSetChanged(parentDishesList);
        } else {
            initDishesRequest(classifyList.get(position));
        }
    }

    /**
     * 初始化菜品请求数据
     *
     * @param bean
     */
    private void initDishesRequest(ClassifyItemBean bean) {
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
        Call<List<DishesBean>> dishesBeanCall = RetrofitRequest.service(Config.HOST).dishes(id, MainActivity.table, orderType);
        dishesBeanCall.enqueue(new Callback<List<DishesBean>>() {
            @Override
            public void onResponse(Call<List<DishesBean>> call, Response<List<DishesBean>> response) {
                List<DishesBean> beanList = response.body();
                if (beanList != null) {
                    dishesMap.put(classifyList.get(defaultItem).getName(), beanList);
                    parentDishesList = beanList;
                    mDishesAdapter.notifyDataSetChanged(beanList);
                }
            }

            @Override
            public void onFailure(Call<List<DishesBean>> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化导航栏
     *
     * @param tabList
     */
    private void initTab(List<TabBean> tabList) {
        if (tabList == null || tabList.size() == 0) {
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

}
