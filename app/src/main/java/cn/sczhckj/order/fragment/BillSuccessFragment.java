package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.R;
import cn.sczhckj.order.adapter.ListAdapter;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.constant.Constant;
import cn.sczhckj.order.data.bean.ListBean;
import cn.sczhckj.order.data.constant.OP;
import cn.sczhckj.order.data.bean.QRCodeBean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.event.CloseServiceEvent;
import cn.sczhckj.order.data.event.QRCodeVerifyEvent;
import cn.sczhckj.order.data.event.SettleAountsTypeEvent;
import cn.sczhckj.order.data.network.RetrofitRequest;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.image.GlideLoading;
import cn.sczhckj.order.overwrite.NoScrollRecyclerview;
import cn.sczhckj.platform.rest.io.RestRequest;
import cn.sczhckj.platform.rest.io.json.JSONRestRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @describe: 二维码支付，包括微信，支付宝
 * @author: Like on 2016/11/15.
 * @Email: 572919350@qq.com
 */

public class BillSuccessFragment extends BaseFragment implements Callback<Bean<QRCodeBean>> {


    @Bind(R.id.code_pay_title)
    TextView codePayTitle;
    @Bind(R.id.code)
    ImageView code;
    @Bind(R.id.evaluate_go)
    Button evaluateGo;
    @Bind(R.id.evaluate_no)
    Button evaluateNo;
    @Bind(R.id.evaluate_choose_parent)
    LinearLayout evaluateChooseParent;
    @Bind(R.id.pay_finish)
    TextView payFinish;
    @Bind(R.id.list_recycler)
    NoScrollRecyclerview listRecycler;

    /**
     * 支付类型
     */
    private int type;

    private ListAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_success, null, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setData(Object object) {
        codePayTitle.setText(object.toString());
    }

    @Override
    public void init() {

    }

    /**
     * 微信，支付宝获取二维码
     *
     * @param type
     */
    public void getCode(int type, int favorableType, List<String> groupons, int giftMoney) {
        this.type = type;
        initView(type);
        postData(type, favorableType, groupons, giftMoney);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化视图
     */
    private void initView(int type) {
        payFinish.setVisibility(View.GONE);
        if (type == Constant.CASH || type == Constant.BANK_CART) {
            code.setVisibility(View.GONE);
            evaluateChooseParent.setVisibility(View.VISIBLE);
        } else {
            code.setVisibility(View.VISIBLE);
            evaluateChooseParent.setVisibility(View.GONE);
        }
    }

    /**
     * 请求方式判断并Post数据
     *
     * @param type
     * @param favorableType
     * @param groupons
     * @param giftMoney
     */
    private void postData(int type, int favorableType, List<String> groupons, int giftMoney) {
        String grouponStr = "";
        for (int i = 0; i < groupons.size(); i++) {
            grouponStr = grouponStr + groupons.get(i) + ",";
        }

        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(deviceId);
        bean.setFavorableType(favorableType);
        bean.setGroup(grouponStr);
        bean.setType(type);
        bean.setGiftMoney(giftMoney);

        RestRequest<RequestCommonBean> restRequest = JSONRestRequest.Builder.build(RequestCommonBean.class)
                .op(OP.ACCOUNTS_PAY)
                .time()
                .bean(bean);

        Call<Bean<QRCodeBean>> codeCall = RetrofitRequest.service().pay(restRequest.toRequestString());
        codeCall.enqueue(this);
    }

    @OnClick({R.id.evaluate_go, R.id.evaluate_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.evaluate_go:
                EventBus.getDefault().post(new SettleAountsTypeEvent(SettleAountsTypeEvent.ETYPE));
                break;
            case R.id.evaluate_no:
                /**不评价的话，返回主界面*/
                finish();
                break;
        }
    }

    @Override
    public void onResponse(Call<Bean<QRCodeBean>> call, Response<Bean<QRCodeBean>> response) {
        Bean<QRCodeBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                initAdapter(bean.getResult().getListBeanList());
            if (type == Constant.ALIPAY || type == Constant.WEIXIN) {
                /**开启服务等待扫码完成*/
                startQRCodeService();

                code.setVisibility(View.VISIBLE);
                evaluateChooseParent.setVisibility(View.GONE);
                GlideLoading.loadingDishes(getContext(), bean.getResult().getUrl(), code);
            }
        }
    }

    @Override
    public void onFailure(Call<Bean<QRCodeBean>> call, Throwable t) {

    }

    private void initAdapter(List<ListBean> bean) {
        mListAdapter = new ListAdapter(getContext(), bean);
        listRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        listRecycler.setAdapter(mListAdapter);
    }

    /**
     * 验证二维码是否成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void verifyServiceEventBus(QRCodeVerifyEvent event){
        if (event.getCode()==ResponseCode.SUCCESS){
            codePayTitle.setText(event.getMsg()+"");
            /**销毁时关闭服务*/
            EventBus.getDefault().post(new CloseServiceEvent());
            code.setVisibility(View.GONE);
            evaluateChooseParent.setVisibility(View.VISIBLE);
        }
    }


}
