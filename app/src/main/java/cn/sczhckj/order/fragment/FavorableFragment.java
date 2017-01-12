package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    @Bind(R.id.loading_progress)
    ProgressBar loadingProgress;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorable, null, false);
        ButterKnife.bind(this, view);
        return view;
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
        loading(loadingParent, favorRecy, loadingItemParent, loadingFail, loadingTitle,
                mContext.getResources().getString(R.string.loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.memberCode);
        mCardMode.info(bean, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.back, R.id.loading_parent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                /**返回*/
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.FAVORABLE_OUT));
                break;
            case R.id.loading_parent:
                /**重新加载*/
                setData(null);
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<CardInfoBean>> call, Response<Bean<CardInfoBean>> response) {
        Bean<CardInfoBean> bean = response.body();

        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            loadingSuccess(loadingParent, favorRecy, loadingItemParent, loadingFail);
            mAdapter.notifyDataSetChanged(bean.getResult().getCards());
        } else {
            loadingFail(loadingParent, favorRecy, loadingItemParent, loadingFail, loadingFailTitle,
                    mContext.getResources().getString(R.string.loadingFail));
        }
    }

    @Override
    public void onFailure(Call<Bean<CardInfoBean>> call, Throwable t) {
        loadingFail(loadingParent, favorRecy, loadingItemParent, loadingFail, loadingFailTitle,
                mContext.getResources().getString(R.string.loadingFail));
    }
}
