package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.VipFavorableAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.CardMode;
import cn.sczhckj.order.overwrite.DashlineItemDivider;
import cn.sczhckj.order.until.AppSystemUntil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 优惠列表界面
 * @author: Like on 2016/11/23.
 * @Email: 572919350@qq.com
 */

public class FavorableFragment extends BaseFragment implements Callback<Bean<CardInfoBean>> {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.favor_recy)
    RecyclerView favorRecy;

    /**
     * 获取数据
     */
    private CardMode mCardMode;

    /**
     * 优惠信息适配器
     */
    private VipFavorableAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_favorable;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initFavor();
    }

    @Override
    public void setData(Object object) {
        initFavor();
    }

    @Override
    public void init() {
        mCardMode = new CardMode();
        initAdapter();
    }

    @Override
    public void initFail() {
        /**重新加载*/
        setData(null);
    }

    @Override
    public void loadingFail() {

    }

    /**
     * 初始化更多优惠适配器
     */
    private void initAdapter() {
        mAdapter = new VipFavorableAdapter(null, mContext);
        favorRecy.setLayoutManager(new LinearLayoutManager(mContext));
        favorRecy.addItemDecoration(new DashlineItemDivider(ContextCompat.getColor(mContext, R.color.cart_line), 100000, 1));
        favorRecy.setAdapter(mAdapter);
    }

    /**
     * 初始化更多优惠获取数据
     */
    private void initFavor() {
        initing(getString(R.string.favorable_fragment__loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        mCardMode.info(bean, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                /**返回*/
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.FAVORABLE_OUT));
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<CardInfoBean>> call, Response<Bean<CardInfoBean>> response) {
        Bean<CardInfoBean> bean = response.body();

        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            initSuccess();
            mAdapter.notifyDataSetChanged(bean.getResult().getCards());
        } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
            initFailer(bean.getMessage());
        } else {
            initFailer(mContext.getResources().getString(R.string.favorable_fragment_loading_fail));
        }
    }

    @Override
    public void onFailure(Call<Bean<CardInfoBean>> call, Throwable t) {
        initFailer(mContext.getResources().getString(R.string.favorable_fragment_loading_fail));
    }
}
