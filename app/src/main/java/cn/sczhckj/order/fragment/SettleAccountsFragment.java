package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
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
import cn.sczhckj.order.adapter.SettleAccountsAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.OP;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.SettleAccountsBean;
import cn.sczhckj.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckj.order.data.event.GrouponVerifyEvent;
import cn.sczhckj.order.data.event.LoginEvent;
import cn.sczhckj.order.data.event.SettleAountsCartEvent;
import cn.sczhckj.order.data.event.SettleAountsTypeEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 结账界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsFragment extends BaseFragment implements Callback<Bean<SettleAccountsBean>> {

    @Bind(R.id.dishes_details)
    ExpandableListView dishesDetails;
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

    private List<SettleAccountsDishesBean> mList = new ArrayList<>();

    private SettleAccountsAdapter mSettleAccountsAdapter;
    /**
     * 优惠类型的ID
     */
    private Integer favorableId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settle_accounts, null, false);
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
        //设置 属性 GroupIndicator 去掉默认向下的箭头
        dishesDetails.setGroupIndicator(null);
        mSettleAccountsAdapter = new SettleAccountsAdapter(getContext(), mList);
        dishesDetails.setAdapter(mSettleAccountsAdapter);
    }

    /**
     * 获取清单数据
     */
    public void getData() {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ACCOUNTS_LIST)
                .time()
                .bean(bean);

        Call<Bean<SettleAccountsBean>> settleAccountsBeanCall = RetrofitRequest.service().settleAccountsList(restRequest.toRequestString());
        settleAccountsBeanCall.enqueue(this);
        loading(getContext().getResources().getString(R.string.loading));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResponse(Call<Bean<SettleAccountsBean>> call, Response<Bean<SettleAccountsBean>> response) {
        Bean<SettleAccountsBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            loadingSuccess();
            mList = bean.getResult().getSettleAccountsDishesBeen();
            mSettleAccountsAdapter.notifyDataSetChanged(mList);
            /**更新左侧结账方式*/
            EventBus.getDefault().post(new SettleAountsCartEvent(SettleAountsCartEvent.LOADING, bean.getResult()));
        } else if (bean != null) {
            loadingFail(bean.getMessage());
        } else {
            loadingFail(getContext().getResources().getString(R.string.loadingFail));
        }

    }

    @Override
    public void onFailure(Call<Bean<SettleAccountsBean>> call, Throwable t) {
        loadingFail(getContext().getResources().getString(R.string.loadingFail));
    }

    /**
     * 获取菜品信息
     *
     * @return
     */
    public List<SettleAccountsDishesBean> getmList() {
        return mList;
    }

    @OnClick(R.id.loadingParent)
    public void onClick() {
        /**加载失败时使用*/
        getData();
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
     * 登陆成功，通知价格刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(LoginEvent event) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setUserId(event.getBean().getId());
        bean.setFavorableType(favorableId);
        favorableVerify(bean);
    }

    /**
     * 团购券验证，通知价格刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void grouponVerifyEventBus(GrouponVerifyEvent event) {
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setGroupList(event.getList());
        bean.setFavorableType(favorableId);
        favorableVerify(bean);
    }

    /**
     * 处理消费者选择的优惠类型，留值等待验证返回新数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void favorableTypeEventBus(SettleAountsTypeEvent event) {
        if (event.getType() == SettleAountsTypeEvent.FTYPE) {
            /**为优惠类型赋值*/
            favorableId = event.getFavorableTypeBean().getId();
        }
    }

    /**
     * 优惠类型验证
     *
     * @param bean
     */
    private void favorableVerify(RequestCommonBean bean) {

        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ACCOUNTS_FAVORABLE_TYPE)
                .time()
                .bean(bean);

        Call<Bean<SettleAccountsBean>> settleAccountsBeanCall = RetrofitRequest.service().favorableTypeVerify(restRequest.toRequestString());
        settleAccountsBeanCall.enqueue(new Callback<Bean<SettleAccountsBean>>() {
            @Override
            public void onResponse(Call<Bean<SettleAccountsBean>> call, Response<Bean<SettleAccountsBean>> response) {
                Bean<SettleAccountsBean> bean = response.body();
                if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                    mList = bean.getResult().getSettleAccountsDishesBeen();
                    mSettleAccountsAdapter.notifyDataSetChanged(mList);
                    /**更新左侧结账方式*/
                    EventBus.getDefault().post(new SettleAountsCartEvent(SettleAountsCartEvent.REFRESH, bean.getResult()));
                }
            }

            @Override
            public void onFailure(Call<Bean<SettleAccountsBean>> call, Throwable t) {

            }
        });
    }

}
