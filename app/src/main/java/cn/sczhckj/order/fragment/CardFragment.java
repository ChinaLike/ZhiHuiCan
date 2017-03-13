package cn.sczhckj.order.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sczhckj.order.Config;
import cn.sczhckj.order.MyApplication;
import cn.sczhckj.order.R;
import cn.sczhckj.order.data.bean.Bean;
import cn.sczhckj.order.data.bean.RequestCommonBean;
import cn.sczhckj.order.data.bean.ResponseCommonBean;
import cn.sczhckj.order.data.bean.card.CardInfoBean;
import cn.sczhckj.order.data.event.SwitchViewEvent;
import cn.sczhckj.order.data.response.ResponseCode;
import cn.sczhckj.order.mode.CardMode;
import cn.sczhckj.order.until.AppSystemUntil;
import cn.sczhckj.order.until.show.T;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @ describe:  办卡界面
 * @ author: Like on 2016/12/26.
 * @ email: 572919350@qq.com
 */

public class CardFragment extends BaseFragment implements Callback<Bean<CardInfoBean>> {


    @Bind(R.id.card_presentation)
    WebView cardPresentation;
    @Bind(R.id.card_input_name)
    EditText cardInputName;
    @Bind(R.id.card_input_name_cancel)
    ImageView cardInputNameCancel;
    @Bind(R.id.card_input_phone)
    EditText cardInputPhone;
    @Bind(R.id.card_input_phone_cancel)
    ImageView cardInputPhoneCancel;
    @Bind(R.id.apply_for_vip_card_confirm)
    Button applyForVipCardConfirm;
    @Bind(R.id.card_info_parent)
    LinearLayout cardInfoParent;
    @Bind(R.id.card_close)
    ImageView cardClose;

    /**
     * 获取数据
     */
    private CardMode mCardMode;
    /**
     * 总体介绍地址
     */
    private String url;
    /**
     * 卡片参数
     */
    private CardInfoBean.Card card;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutId() {
        return R.layout.fragment_card;
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
     * 初始化更多优惠获取数据
     */
    private void initFavor() {
        initing(mContext.getResources().getString(R.string.card_fragment_loading));
        RequestCommonBean bean = new RequestCommonBean();
        bean.setDeviceId(AppSystemUntil.getAndroidID(mContext));
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        mCardMode.info(bean, this);
    }

    /**
     * 办卡
     *
     * @param card
     */
    public void card(CardInfoBean.Card card) {
        this.card = card;
        isShow(false);
        loadingWeb(card.getUrl());
    }

    /**
     * 提交信息
     */
    private void commit() {
        applyForVipCardConfirm.setText(getString(R.string.card_fragment_commit));
        applyForVipCardConfirm.setClickable(false);
        RequestCommonBean bean = new RequestCommonBean();
        bean.setMemberCode(MyApplication.tableBean.getUser() == null ? "" : MyApplication.tableBean.getUser().getMemberCode());
        bean.setName(cardInputName.getText().toString());
        bean.setPhone(cardInputPhone.getText().toString());
        bean.setCardId(card.getCardId());
        mCardMode.apply(bean, applyCallback);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.card_input_name_cancel, R.id.card_input_phone_cancel, R.id.apply_for_vip_card_confirm, R.id.card_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_input_name_cancel:
                /**清除名字输入*/
                cardInputName.setText("");
                break;
            case R.id.card_input_phone_cancel:
                /**清除电话输入*/
                cardInputPhone.setText("");
                break;
            case R.id.apply_for_vip_card_confirm:
                /**确定*/
                commit();
                break;
            case R.id.card_close:
                /**关闭*/
                isShow(true);
                loadingWeb(url);
                break;
        }
    }

    /**
     * 显示主页
     *
     * @param isShow
     */
    private void isShow(boolean isShow) {
        if (isShow) {
            cardInfoParent.setVisibility(View.GONE);
            cardClose.setVisibility(View.GONE);
        } else {
            cardInfoParent.setVisibility(View.VISIBLE);
            cardClose.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载网页
     *
     * @param url
     */
    private void loadingWeb(String url) {
        cardPresentation.loadUrl(Config.HOST+url);
        cardPresentation.setHorizontalScrollBarEnabled(false);
        cardPresentation.setVerticalScrollBarEnabled(false);
        cardPresentation.getSettings().setJavaScriptEnabled(true);
        cardPresentation.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onResponse(Call<Bean<CardInfoBean>> call, Response<Bean<CardInfoBean>> response) {
        Bean<CardInfoBean> bean = response.body();
        if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
            initSuccess();
            isShow(true);
            url = bean.getResult().getUrl();
            loadingWeb(url);
        } else if ((bean != null && bean.getCode() == ResponseCode.FAILURE)) {
            initFailer(bean.getMessage());
        } else {
            initFailer(mContext.getResources().getString(R.string.card_fragment_loading_fail));
        }
    }

    @Override
    public void onFailure(Call<Bean<CardInfoBean>> call, Throwable t) {
        initFailer(mContext.getResources().getString(R.string.card_fragment_loading_fail));
    }

    /**
     * 提交信息回调
     */
    Callback<Bean<ResponseCommonBean>> applyCallback = new Callback<Bean<ResponseCommonBean>>() {
        @Override
        public void onResponse(Call<Bean<ResponseCommonBean>> call, Response<Bean<ResponseCommonBean>> response) {
            Bean<ResponseCommonBean> bean = response.body();
            applyForVipCardConfirm.setClickable(true);
            if (bean != null && bean.getCode() == ResponseCode.SUCCESS) {
                applyForVipCardConfirm.setText("确定");
                T.showShort(mContext, bean.getMessage());
                EventBus.getDefault().post(new SwitchViewEvent(SwitchViewEvent.FAVORABLE_OUT));
            } else if (bean != null && bean.getCode() == ResponseCode.FAILURE) {
                applyForVipCardConfirm.setText(bean.getMessage());
            } else {
                applyForVipCardConfirm.setText(getString(R.string.card_fragment_commit_fail));
            }
        }

        @Override
        public void onFailure(Call<Bean<ResponseCommonBean>> call, Throwable t) {
            applyForVipCardConfirm.setText(getString(R.string.card_fragment_commit_fail));
            applyForVipCardConfirm.setClickable(true);
        }
    };

}
