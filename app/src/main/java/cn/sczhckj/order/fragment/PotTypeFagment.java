package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import cn.sczhckj.order.activity.MainActivity;
import cn.sczhckj.order.adapter.PersonChooseAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.DishesBean;
import cn.sczhckj.order.data.bean.MainPagerShow;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.PersonBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.CartNumberEvent;
import cn.sczhckj.order.data.listener.OnTableListenner;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 锅底选择和推荐菜品
 * @author: Like on 2016/11/4.
 * @Email: 572919350@qq.com
 */

public class PotTypeFagment extends BaseFragment implements Callback<Bean<MainPagerShow>> {


    @Bind(R.id.pot_line)
    View potLine;
    @Bind(R.id.dishes_line)
    View dishesLine;
    @Bind(R.id.person_choose)
    RecyclerView personChoose;
    @Bind(R.id.dishes_choose)
    RecyclerView dishesChoose;
    @Bind(R.id.rl_pot_parent)
    RelativeLayout potParent;
    @Bind(R.id.rl_order_dishes_parent)
    RelativeLayout dishesParent;

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
     * 默认显示几列备选人数
     */
    public static int DEFAULT_PERSON = 3;


    private PersonChooseAdapter personAdapter;

    private List<PersonBean> personList=new ArrayList<>();

    private List<DishesBean> dishesList;

    private OnTableListenner mOnTableListenner;

    /**
     * 用户ID
     */
    private String userId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**注册事件监听*/
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pot_type, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        potParent.setClickable(false);
        dishesParent.setClickable(false);
        initDishesAdapter(dishesChoose);
    }

    /**
     * 获取开桌显示数据
     */
    private void initNet() {
        try {
            deviceId = AppSystemUntil.getAndroidID(getContext());
            RequestCommonBean bean = new RequestCommonBean();
            bean.setId(userId);
            bean.setDeviceId(deviceId);
            RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                    .op(OP.OPEN_TABLE_SHOW)
                    .time()
                    .bean(bean);
            Call<Bean<MainPagerShow>> mainShow = RetrofitRequest.service().potDataShow(restRequest.toRequestString());
            mainShow.enqueue(this);
            loading(getContext().getResources().getString(R.string.loading));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setData(Object object) {

    }

    @Override
    public void onResume() {
        super.onResume();
        initNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化界面
     */
    private void initView(MainPagerShow bean) {
        potParent.setClickable(true);
        dishesParent.setClickable(true);
        if (bean.getPerson()!=null){
            DEFAULT_PERSON = bean.getPerson().size();
            DEFAULT_PERSON=DEFAULT_PERSON>=0?DEFAULT_PERSON:0;
        }else {
            DEFAULT_PERSON = 0;
        }
        /**========初始化人数========*/
        personChoose.setLayoutManager(new LinearLayoutManager(getActivity()));
        personAdapter = new PersonChooseAdapter(getActivity(), initPerson(bean));
        personChoose.setAdapter(personAdapter);
        /**设置分割线*/
        personChoose.addItemDecoration(new DashlineItemDivider(getResources().getColor(R.color.line_s), 5, 1, DEFAULT_PERSON));
        /**========初始化菜品========*/
        dishesList = bean.getDishesList();
        parentDishesList = bean.getPotList();
        mDishesAdapter.notifyDataSetChanged(parentDishesList);
    }


    /**
     * 购物车数据变化后选择栏的数据同步刷新
     *
     * @param bean
     */
    public void upData(DishesBean bean) {
        String id = bean.getId();
        String dishesName = bean.getName();
        for (DishesBean item : dishesList) {
            if (item.getId().equals(id) && item.getName().equals(dishesName)) {
                item.setNumber(bean.getNumber());
                mDishesAdapter.notifyDataSetChanged();
            }
        }
        for (DishesBean item : parentDishesList) {
            if (item.getId().equals(id) && item.getName().equals(dishesName)) {
                item.setNumber(bean.getNumber());
                mDishesAdapter.notifyDataSetChanged();
            }
        }

    }

    /**
     * 初始化桌面人数
     *
     * @param bean
     * @return
     */
    private List<PersonBean> initPerson(MainPagerShow bean) {
        /**添加人数选择*/
        PersonBean mPersonBean = new PersonBean();
        mPersonBean.setNumber(bean.getDefaultNumber());
        mPersonBean.setTableName(bean.getTableNumber());
        /**初始化默认人数，避免消费者未选择时此数为0*/
        MainActivity.person = bean.getDefaultNumber();
        if (bean.getPerson()!=null) {
            personList = bean.getPerson();
        }
        personList.add(mPersonBean);
        return personList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        /**取消事件监听*/
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cartEventBus(CartNumberEvent event) {
        upData(event.getBean());
    }

    @OnClick({R.id.rl_pot_parent, R.id.rl_order_dishes_parent,R.id.loadingParent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_pot_parent:
                potLine.setVisibility(View.VISIBLE);
                dishesLine.setVisibility(View.GONE);
                mDishesAdapter.notifyDataSetChanged(parentDishesList);
                break;
            case R.id.rl_order_dishes_parent:
                potLine.setVisibility(View.GONE);
                dishesLine.setVisibility(View.VISIBLE);
                mDishesAdapter.notifyDataSetChanged(dishesList);
                break;
            case R.id.loadingParent:
                /**点击重新加载数据*/
                initNet();
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
    public void onResponse(Call<Bean<MainPagerShow>> call, Response<Bean<MainPagerShow>> response) {
        Bean<MainPagerShow> bean = response.body();
        /**获取数据成功*/
        if (bean != null && bean.getCode() == 0) {
            loadingSuccess();
            initView(bean.getResult());
            mOnTableListenner.table(bean.getResult().getTableNumber(), bean.getResult().getWaitress());
            /**设置开桌密码*/
            openTablePassword=bean.getResult().getOpenTablePassword();
        }else if (bean != null) {
            loadingFail(bean.getMessage());
        } else {
            loadingFail(getContext().getResources().getString(R.string.loadingFail));
        }
    }

    @Override
    public void onFailure(Call<Bean<MainPagerShow>> call, Throwable t) {
        loadingFail(getContext().getResources().getString(R.string.loadingFail));
    }

    /**
     * 加载中
     */
    private void loading(String str){
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
        loadingTitle.setText(str);
    }

    /**
     * 加载成功
     */
    private void loadingSuccess(){
        loadingParent.setVisibility(View.GONE);
        contextParent.setVisibility(View.VISIBLE);
        loadingItemParent.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    private void loadingFail(String str){
        loadingParent.setVisibility(View.VISIBLE);
        contextParent.setVisibility(View.GONE);
        loadingItemParent.setVisibility(View.GONE);
        loadingFail.setVisibility(View.VISIBLE);
        loadingFailTitle.setText(str);
    }
}
