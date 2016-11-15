package cn.sczhckg.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sczhckg.order.Config;
import cn.sczhckg.order.R;
import cn.sczhckg.order.activity.MainActivity;
import cn.sczhckg.order.adapter.SettleAccountsAdapter;
import cn.sczhckg.order.data.bean.SettleAccountsBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesBean;
import cn.sczhckg.order.data.bean.SettleAccountsDishesItemBean;
import cn.sczhckg.order.data.event.SettleAountsCartEvent;
import cn.sczhckg.order.data.network.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 结账界面
 * @author: Like on 2016/11/9.
 * @Email: 572919350@qq.com
 */

public class SettleAccountsFragment extends BaseFragment implements Callback<SettleAccountsBean> {

    @Bind(R.id.dishes_details)
    ExpandableListView dishesDetails;

    private List<SettleAccountsDishesBean> mList = new ArrayList<>();

    private SettleAccountsAdapter mSettleAccountsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Call<SettleAccountsBean> settleAccountsBeanCall = RetrofitRequest.service(Config.HOST).settleAccountsList(MainActivity.table);
        settleAccountsBeanCall.enqueue(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResponse(Call<SettleAccountsBean> call, Response<SettleAccountsBean> response) {
        SettleAccountsBean bean = response.body();
        if (bean != null) {
            mList = bean.getSettleAccountsDishesBeen();
            mSettleAccountsAdapter.notifyDataSetChanged(mList);
            /**更新左侧结账方式*/
            EventBus.getDefault().post(new SettleAountsCartEvent(SettleAountsCartEvent.LOADING, bean.getFavorableType(), bean.getPayTypeBeen()));
        }

    }

    @Override
    public void onFailure(Call<SettleAccountsBean> call, Throwable t) {
        Toast.makeText(getContext(), getString(R.string.overTime), Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取菜品信息
     *
     * @return
     */
    public List<SettleAccountsDishesBean> getmList() {
        return mList;
    }
}
